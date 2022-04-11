package project.px.service;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.px.entity.*;
import project.px.repository.InvoiceRepository;
import project.px.repository.MartRepository;
import project.px.repository.ProductRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import static project.px.entity.QInvoice.*;

@Service
@RequiredArgsConstructor
@Transactional
public class InvoiceService {

    private final JPAQueryFactory queryFactory;

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

}
