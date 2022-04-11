package project.px.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.util.Pair;
import org.springframework.transaction.annotation.Transactional;
import project.px.entity.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class InvoiceServiceTest {

    @PersistenceContext
    EntityManager em;

    @Autowired
    InvoiceService invoiceService;

    @Test
    public void invoice() {
        Mart mart = new Mart("mart1", "110000", "110000!", MartLevel.A);
        em.persist(mart);
        Long martId = mart.getId();

        TransportCompany transportCompany = new TransportCompany("tc1");
        ProductCompany productCompany = new ProductCompany("pc1");

        em.persist(transportCompany);
        em.persist(productCompany);

        Category category = new Category("snack");

        em.persist(category);

        ArrayList<Pair<Long, Integer>> products = new ArrayList<>();

        for (int i = 1; i <= 10; i++) {
            Product product = new Product("snack" + i, 800 + 100*i, 180 + 30*i, 12 + 6*i, null, productCompany, transportCompany, category, ContractStatus.CONTRACTED, DemandStatus.HIGH, ProductLevel.A, TransportDay.FRI);
            em.persist(product);
            Pair<Long, Integer> pair = Pair.of(product.getId(), i);
            products.add(pair);
        }
        Long invoiceId = invoiceService.invoice(martId, products);
        Invoice invoice = em.find(Invoice.class, invoiceId);

        assertThat(invoice.getInvoiceProducts().size()).isEqualTo(10);
        assertThat(invoice.getMart()).isEqualTo(mart);
    }

    @Test
    public void addInvoiceProductsAlreadyExistProduct() {
        Mart mart = new Mart("mart1", "110000", "110000!", MartLevel.A);
        em.persist(mart);
        Long martId = mart.getId();

        TransportCompany transportCompany = new TransportCompany("tc1");
        ProductCompany productCompany = new ProductCompany("pc1");

        em.persist(transportCompany);
        em.persist(productCompany);

        Category category = new Category("snack");

        em.persist(category);

        ArrayList<Pair<Long, Integer>> products = new ArrayList<>();

        for (int i = 1; i <= 10; i++) {
            Product product = new Product("snack" + i,
                    800 + 100*i,
                    180 + 30*i,
                    12 + 6*i,
                    null,
                    productCompany,
                    transportCompany,
                    category,
                    ContractStatus.CONTRACTED,
                    DemandStatus.HIGH,
                    ProductLevel.A,
                    TransportDay.FRI);
            em.persist(product);
            Pair<Long, Integer> pair = Pair.of(product.getId(), i);
            products.add(pair);
        }
        Long invoiceId = invoiceService.invoice(martId, products);

        em.flush();
        em.clear();

        invoiceService.addInvoiceProducts(invoiceId, products);

        Invoice findInvoice = em.find(Invoice.class, invoiceId);
        List<InvoiceProduct> findInvoiceProducts = findInvoice.getInvoiceProducts();

        assertThat(findInvoiceProducts.size()).isEqualTo(10);
        assertThat(findInvoiceProducts).extracting(InvoiceProduct::getCount).containsExactly(2, 4, 6, 8, 10, 12, 14, 16, 18, 20);
    }

    @Test
    public void addInvoiceProductsNewProduct() {
        Mart mart = new Mart("mart1", "110000", "110000!", MartLevel.A);
        em.persist(mart);
        Long martId = mart.getId();

        TransportCompany transportCompany = new TransportCompany("tc1");
        ProductCompany productCompany = new ProductCompany("pc1");

        em.persist(transportCompany);
        em.persist(productCompany);

        Category category = new Category("snack");

        em.persist(category);

        ArrayList<Pair<Long, Integer>> products = new ArrayList<>();

        for (int i = 1; i <= 10; i++) {
            Product product = new Product("snack" + i,
                    800 + 100*i,
                    180 + 30*i,
                    12 + 6*i,
                    null,
                    productCompany,
                    transportCompany,
                    category,
                    ContractStatus.CONTRACTED,
                    DemandStatus.HIGH,
                    ProductLevel.A,
                    TransportDay.FRI);
            em.persist(product);
            Pair<Long, Integer> pair = Pair.of(product.getId(), i);
            products.add(pair);
        }
        Long invoiceId = invoiceService.invoice(martId, products);

        em.flush();
        em.clear();

        ArrayList<Pair<Long, Integer>> addProducts = new ArrayList<>();
        for (int i = 11; i <= 20; i++) {
            Product product = new Product("snack" + i,
                    800 + 100*i,
                    180 + 30*i,
                    12 + 6*i,
                    null,
                    productCompany,
                    transportCompany,
                    category,
                    ContractStatus.CONTRACTED,
                    DemandStatus.HIGH,
                    ProductLevel.A,
                    TransportDay.FRI);
            em.persist(product);
            Pair<Long, Integer> pair = Pair.of(product.getId(), i);
            addProducts.add(pair);
        }

        invoiceService.addInvoiceProducts(invoiceId, addProducts);

        Invoice findInvoice = em.find(Invoice.class, invoiceId);
        List<InvoiceProduct> findInvoiceProducts = findInvoice.getInvoiceProducts();

        assertThat(findInvoiceProducts.size()).isEqualTo(20);
        assertThat(findInvoiceProducts).extracting(InvoiceProduct::getCount).containsExactly(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20);

    }

    @Test
    public void reduceInvoiceProductsAlreadyExistProduct() {
        Mart mart = new Mart("mart1", "110000", "110000!", MartLevel.A);
        em.persist(mart);
        Long martId = mart.getId();

        TransportCompany transportCompany = new TransportCompany("tc1");
        ProductCompany productCompany = new ProductCompany("pc1");

        em.persist(transportCompany);
        em.persist(productCompany);

        Category category = new Category("snack");

        em.persist(category);

        ArrayList<Pair<Long, Integer>> products = new ArrayList<>();

        for (int i = 1; i <= 10; i++) {
            Product product = new Product("snack" + i,
                    800 + 100*i,
                    180 + 30*i,
                    12 + 6*i,
                    null,
                    productCompany,
                    transportCompany,
                    category,
                    ContractStatus.CONTRACTED,
                    DemandStatus.HIGH,
                    ProductLevel.A,
                    TransportDay.FRI);
            em.persist(product);
            Pair<Long, Integer> pair = Pair.of(product.getId(), i);
            products.add(pair);
        }
        Long invoiceId = invoiceService.invoice(martId, products);

        em.flush();
        em.clear();

        invoiceService.reduceInvoiceProducts(invoiceId, products);

        Invoice findInvoice = em.find(Invoice.class, invoiceId);
        List<InvoiceProduct> findInvoiceProducts = findInvoice.getInvoiceProducts();

        assertThat(findInvoiceProducts.size()).isEqualTo(10);
        assertThat(findInvoiceProducts).extracting(InvoiceProduct::getCount).containsExactly(0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
    }

    @Test
    public void reduceInvoiceProductsNewProduct() {
        Mart mart = new Mart("mart1", "110000", "110000!", MartLevel.A);
        em.persist(mart);
        Long martId = mart.getId();

        TransportCompany transportCompany = new TransportCompany("tc1");
        ProductCompany productCompany = new ProductCompany("pc1");

        em.persist(transportCompany);
        em.persist(productCompany);

        Category category = new Category("snack");

        em.persist(category);

        ArrayList<Pair<Long, Integer>> products = new ArrayList<>();

        for (int i = 1; i <= 10; i++) {
            Product product = new Product("snack" + i,
                    800 + 100*i,
                    180 + 30*i,
                    12 + 6*i,
                    null,
                    productCompany,
                    transportCompany,
                    category,
                    ContractStatus.CONTRACTED,
                    DemandStatus.HIGH,
                    ProductLevel.A,
                    TransportDay.FRI);
            em.persist(product);
            Pair<Long, Integer> pair = Pair.of(product.getId(), i);
            products.add(pair);
        }
        Long invoiceId = invoiceService.invoice(martId, products);

        em.flush();
        em.clear();

        ArrayList<Pair<Long, Integer>> addProducts = new ArrayList<>();
        for (int i = 11; i <= 20; i++) {
            Product product = new Product("snack" + i,
                    800 + 100*i,
                    180 + 30*i,
                    12 + 6*i,
                    null,
                    productCompany,
                    transportCompany,
                    category,
                    ContractStatus.CONTRACTED,
                    DemandStatus.HIGH,
                    ProductLevel.A,
                    TransportDay.FRI);
            em.persist(product);
            Pair<Long, Integer> pair = Pair.of(product.getId(), i);
            addProducts.add(pair);
        }

        invoiceService.reduceInvoiceProducts(invoiceId, addProducts);

        Invoice findInvoice = em.find(Invoice.class, invoiceId);
        List<InvoiceProduct> findInvoiceProducts = findInvoice.getInvoiceProducts();

        assertThat(findInvoiceProducts.size()).isEqualTo(10);
        assertThat(findInvoiceProducts).extracting(InvoiceProduct::getCount).containsExactly(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

    }

    @Test
    void findAll() {
        Mart mart = new Mart("mart1", "110000", "110000!", MartLevel.A);
        em.persist(mart);
        Long martId = mart.getId();

        TransportCompany transportCompany = new TransportCompany("tc1");
        ProductCompany productCompany = new ProductCompany("pc1");

        em.persist(transportCompany);
        em.persist(productCompany);

        Category category = new Category("snack");

        em.persist(category);

        ArrayList<Pair<Long, Integer>> products = new ArrayList<>();

        for (int i = 1; i <= 10; i++) {
            Product product = new Product("snack" + i,
                    800 + 100*i,
                    180 + 30*i,
                    12 + 6*i,
                    null,
                    productCompany,
                    transportCompany,
                    category,
                    ContractStatus.CONTRACTED,
                    DemandStatus.HIGH,
                    ProductLevel.A,
                    TransportDay.FRI);
            em.persist(product);
            Pair<Long, Integer> pair = Pair.of(product.getId(), i);
            products.add(pair);
        }
        Long invoiceId = invoiceService.invoice(martId, products);

        em.flush();
        em.clear();

        List<Invoice> invoices = invoiceService.findInvoices();

        assertThat(invoices.size()).isEqualTo(1);
    }

    @Test
    void findOne() {
        Mart mart = new Mart("mart1", "110000", "110000!", MartLevel.A);
        em.persist(mart);
        Long martId = mart.getId();

        TransportCompany transportCompany = new TransportCompany("tc1");
        ProductCompany productCompany = new ProductCompany("pc1");

        em.persist(transportCompany);
        em.persist(productCompany);

        Category category = new Category("snack");

        em.persist(category);

        ArrayList<Pair<Long, Integer>> products = new ArrayList<>();

        for (int i = 1; i <= 10; i++) {
            Product product = new Product("snack" + i,
                    800 + 100*i,
                    180 + 30*i,
                    12 + 6*i,
                    null,
                    productCompany,
                    transportCompany,
                    category,
                    ContractStatus.CONTRACTED,
                    DemandStatus.HIGH,
                    ProductLevel.A,
                    TransportDay.FRI);
            em.persist(product);
            Pair<Long, Integer> pair = Pair.of(product.getId(), i);
            products.add(pair);
        }
        Long invoiceId = invoiceService.invoice(martId, products);

        Optional<Invoice> one = invoiceService.findOne(invoiceId);
        assertThat(one.orElse(null)).isNotNull();
    }

    @Test
    void findOneNotExist() {
        Optional<Invoice> one = invoiceService.findOne(144L);
        assertThat(one.orElse(null)).isNull();
    }

}