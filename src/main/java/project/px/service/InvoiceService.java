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

    public Long invoice(Long martId, List<Pair<Long, Integer>> products) {
        Mart mart =  martRepository.findById(martId).orElse(null);
        if (mart == null) {
            return -1L;
        }
        List<InvoiceProduct> invoiceProducts = new ArrayList<>();

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

    public void addInvoiceProducts(Long invoiceId, List<Pair<Long, Integer>> products) {
        Invoice invoice = invoiceRepository.findByIdFetchJoinInvoiceProductsAndProduct(invoiceId);
        if (invoice == null) {
            return;
        }
        List<InvoiceProduct> invoiceProducts = invoice.getInvoiceProducts();

        for (Pair<Long, Integer> p : products) {
            boolean isExist = false;
            for (InvoiceProduct invoiceProduct : invoiceProducts) {
                if (invoiceProduct.getProduct().getId().equals(p.getFirst())) {
                    invoiceProduct.addCount(p.getSecond());
                    isExist = true;
                    break;
                }
            }
            if (!isExist) {
                Product product = productRepository.findById(p.getFirst()).orElse(null);
                if (product == null) {
                    break;
                }
                invoiceProducts.add(new InvoiceProduct(p.getSecond(), product));
            }
        }
    }

    public void reduceInvoiceProducts(Long invoiceId, List<Pair<Long, Integer>> products) {
        Invoice invoice = invoiceRepository.findByIdFetchJoinInvoiceProductsAndProduct(invoiceId);
        if (invoice == null) {
            return;
        }
        List<InvoiceProduct> invoiceProducts = invoice.getInvoiceProducts();

        for (Pair<Long, Integer> p : products) {
            for (InvoiceProduct invoiceProduct : invoiceProducts) {
                if (invoiceProduct.getProduct().getId().equals(p.getFirst())) {
                    invoiceProduct.reduceCount(p.getSecond());
                    break;
                }
            }
        }
    }

    public Optional<Invoice> findOne(Long invoiceId) {
        return invoiceRepository.findById(invoiceId);
    }

    public List<Invoice> findInvoices() {
        return invoiceRepository.findAll();
    }

    public List<StockProduct> receiveInvoiceProducts(Long martId) {
        List<Invoice> invoices = invoiceRepository.findInvoicesForReceiveInvoiceProducts(martId);

        for (Invoice invoice : invoices) {
            LocalDate arriveDate = invoice.getArriveDate();
            LocalDate current = LocalDate.now();
            if (arriveDate.isEqual(current) || arriveDate.isBefore(current)) {
                List<InvoiceProduct> invoiceProducts = invoice.getInvoiceProducts();
                List<StockProduct> stockProducts = invoice.getMart().getStockProducts();
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
                invoice.deliveryArrived();
            }
        }
        return null;
    }

    public void cancelInvoice(Long invoiceId) {
        Invoice invoice = invoiceRepository.findById(invoiceId).orElse(null);
        if (invoice == null) {
            return;
        }
        invoice.invoiceCancelled();
    }

}
