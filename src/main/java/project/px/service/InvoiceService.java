package project.px.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.px.entity.*;
import project.px.repository.InvoiceRepository;
import project.px.repository.MartRepository;
import project.px.repository.ProductRepository;
import project.px.repository.StockProductRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class InvoiceService {

    private final StockProductRepository stockProductRepository;
    private final InvoiceRepository invoiceRepository;
    private final MartRepository martRepository;
    private final ProductRepository productRepository;

    // Creating new invoice
    // products contains pairs of productId and count
    public Long invoice(Long martId, List<Pair<Long, Integer>> products) {
        Mart mart =  martRepository.findById(martId).orElse(null);
        // it is not determined how to handle the case when mart does not exist
        if (mart == null) {
            return -1L;
        }
        List<InvoiceProduct> invoiceProducts = new ArrayList<>();

        // add invoiceProducts
        for (Pair<Long, Integer> p : products) {
            Product product = productRepository.findById(p.getFirst()).orElse(null);
            if (product == null) {
                continue;
            }
            invoiceProducts.add(new InvoiceProduct(p.getSecond(), product));
        }

        Invoice invoice = Invoice.createInvoice(mart, invoiceProducts);

        invoiceRepository.save(invoice);

        return invoice.getId();
    }

    // add InvoiceProducts to the already exist invoice
    // products contains pairs of productId and count
    public void addInvoiceProducts(Long invoiceId, List<Pair<Long, Integer>> products) {
        Invoice invoice = invoiceRepository.findByIdFetchJoinInvoiceProductsAndProduct(invoiceId);
        // it is not determined how to handle the case when invoice does not exist
        if (invoice == null) {
            return;
        }
        List<InvoiceProduct> invoiceProducts = invoice.getInvoiceProducts();

        // 1. isExist => product is already exist int he invoiceProducts => find same product in invoiceProducts and just add count.
        // 2. !isExist => new product in the point of view of invoiceProducts => add new product with count,
        for (Pair<Long, Integer> p : products) {
            boolean isExist = false;
            for (InvoiceProduct invoiceProduct : invoiceProducts) {
                if (invoiceProduct.getProduct().getId().equals(p.getFirst())) {
                    invoiceProduct.addCount(p.getSecond());
                    isExist = true;
                    continue;
                }
            }
            if (!isExist) {
                Product product = productRepository.findById(p.getFirst()).orElse(null);
                if (product == null) {
                    break;
                }
                invoice.addInvoiceProduct(new InvoiceProduct(p.getSecond(), product));
            }
        }
    }

    // reduce InvoiceProducts to the already exist invoice
    // products contains pairs of productId and count
    public void reduceInvoiceProducts(Long invoiceId, List<Pair<Long, Integer>> products) {
        // Get invoice with invoiceProducts and products of invoiceProducts with one query.
        Invoice invoice = invoiceRepository.findByIdFetchJoinInvoiceProductsAndProduct(invoiceId);

        // it is not determined how to handle the case when invoice does not exist
        if (invoice == null) {
            return;
        }
        List<InvoiceProduct> invoiceProducts = invoice.getInvoiceProducts();

        // reduce the count. it is not determined when product is not exist in the invoiceProducts
        for (Pair<Long, Integer> p : products) {
            for (InvoiceProduct invoiceProduct : invoiceProducts) {
                if (invoiceProduct.getProduct().getId().equals(p.getFirst())) {
                    invoiceProduct.reduceCount(p.getSecond());
                    break;
                }
            }
        }
    }

    @Transactional(readOnly = true)
    public Optional<Invoice> findOne(Long invoiceId) {
        return invoiceRepository.findById(invoiceId);
    }

    @Transactional(readOnly = true)
    public List<Invoice> findInvoices() {
        return invoiceRepository.findAll();
    }

    // Receive the products from the invoices
    // It is not determined to put this function to the invoiceService or martService
    public List<StockProduct> receiveInvoiceProducts(Long martId) {
        // Get invoice with mart, invoiceProducts, and products of invoiceProducts with one query.
        List<Invoice> invoices = invoiceRepository.findInvoicesForReceiveInvoiceProducts(martId);

        for (Invoice invoice : invoices) {
            LocalDate arriveDate = invoice.getArriveDate();
            LocalDate current = LocalDate.now();
            // Receiving the product is possible when today is equal to or after arriveDate of the invoice
            // Later, consider the checking arriveDate in the query
            if (arriveDate.isEqual(current) || arriveDate.isBefore(current)) {
                List<InvoiceProduct> invoiceProducts = invoice.getInvoiceProducts();
                List<StockProduct> stockProducts = invoice.getMart().getStockProducts();
                // Later, consider the n-ary search to make more performances
                for (InvoiceProduct invoiceProduct : invoiceProducts) {
                    boolean isStockExist = false;
                    for (StockProduct stockProduct : stockProducts) {
                        if (stockProduct.getProduct().getId().equals(invoiceProduct.getProduct().getId())) {
                            isStockExist = true;
                            stockProduct.addCount(invoiceProduct.getCount());
                            break;
                        }
                    }
                    if (!isStockExist) {
                        stockProductRepository.save(new StockProduct(invoiceProduct.getCount(), invoiceProduct.getProduct(), invoice.getMart()));
                    }
                }
                // InvoiceStatus.ARRIVED
                invoice.deliveryArrived();
            }
        }
        return null;
    }

    // Cancel the invoice
    // Invoice is not deleted and saved in the DB
    public void cancelInvoice(Long invoiceId) {
        Invoice invoice = invoiceRepository.findById(invoiceId).orElse(null);
        // it is not determined how to handle the case when invoice does not exist
        if (invoice == null) {
            return;
        }
        // InvoiceStatus.CANCELLED
        invoice.invoiceCancelled();
    }

}
