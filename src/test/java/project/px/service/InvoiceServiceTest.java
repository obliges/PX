package project.px.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.util.Pair;
import org.springframework.transaction.annotation.Transactional;
import project.px.entity.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class InvoiceServiceTest {

    @PersistenceContext
    EntityManager em;

    @Autowired
    InvoiceService invoiceService;
    @Autowired
    MartService martService;


    // Test 1 : Creating Invoice and Save to DB
    @Test
    public void invoice() {
        // Start of Creating One Invoice
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

        // (name, count) => (snack1, 1), (snack2, 2), ... , (snack10, 10)
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
                    ProductLevel.A);
            em.persist(product);
            Pair<Long, Integer> pair = Pair.of(product.getId(), i);
            products.add(pair);
        }
        Long invoiceId = invoiceService.invoice(martId, products);
        // End of Creating One Invoice

        em.flush();
        em.clear();

        Invoice invoice = em.find(Invoice.class, invoiceId);

        // Invoice should not be null
        assertThat(invoice).isNotNull();
        // There should be 10 invoiceProducts
        assertThat(invoice.getInvoiceProducts().size()).isEqualTo(10);
    }

    // Test 2 : Add Invoice Products that already existing in the existing invoice
    @Test
    public void addInvoiceProductsAlreadyExistProduct() {
        // Start of Creating One Invoice
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

        // (name, count) => (snack1, 1), (snack2, 2), ... , (snack10, 10)
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
                    ProductLevel.A);
            em.persist(product);
            Pair<Long, Integer> pair = Pair.of(product.getId(), i);
            products.add(pair);
        }
        Long invoiceId = invoiceService.invoice(martId, products);
        // End of Creating One Invoice

        // Add InvoiceProducts with the same with the existing InvoiceProducts
        invoiceService.addInvoiceProducts(invoiceId, products);

        em.flush();
        em.clear();

        Invoice invoice = em.find(Invoice.class, invoiceId);

        // The size of invoiceProducts should be 10
        assertThat(invoice.getInvoiceProducts().size()).isEqualTo(10);
        // The count of the invoiceProducts should be doubled.
        assertThat(invoice.getInvoiceProducts()).extracting(InvoiceProduct::getCount).containsExactly(2, 4, 6, 8, 10, 12, 14, 16, 18, 20);
    }

    // Test 3 : Add Invoice Products that not  existing in the existing invoice
    @Test
    public void addInvoiceProductsNewProduct() {
        // Start of Creating One Invoice
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

        // (name, count) => (snack1, 1), (snack2, 2), ... , (snack10, 10)
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
                    ProductLevel.A);
            em.persist(product);
            Pair<Long, Integer> pair = Pair.of(product.getId(), i);
            products.add(pair);
        }
        Long invoiceId = invoiceService.invoice(martId, products);
        // End of Creating One Invoice

        // Start of the adding new products to the invoiceProducts that do not exist before
        ArrayList<Pair<Long, Integer>> addProducts = new ArrayList<>();

        // (name, count) => (snack11, 11), (snack12, 12), ... , (snack20, 20)
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
                    ProductLevel.A);
            em.persist(product);
            Pair<Long, Integer> pair = Pair.of(product.getId(), i);
            addProducts.add(pair);
        }

        invoiceService.addInvoiceProducts(invoiceId, addProducts);
        // End of the adding new products to the invoiceProducts that do not exist before

        em.flush();
        em.clear();

        Invoice invoice = em.find(Invoice.class, invoiceId);

        // The size of the invoiceProducts should be 20
        assertThat(invoice.getInvoiceProducts().size()).isEqualTo(20);
        // The count of the invoiceProducts should be 1 ~ 20
        assertThat(invoice.getInvoiceProducts()).extracting(InvoiceProduct::getCount).containsExactly(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20);

    }

    // Test 4 : reduce the Invoice Products that already existing int the existing invoice
    @Test
    public void reduceInvoiceProductsAlreadyExistProduct() {
        // Start of Creating One Invoice
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

        // (name, count) => (snack1, 1), (snack2, 2), ... , (snack10, 10)
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
                    ProductLevel.A);
            em.persist(product);
            Pair<Long, Integer> pair = Pair.of(product.getId(), i);
            products.add(pair);
        }
        Long invoiceId = invoiceService.invoice(martId, products);
        // End of Creating One Invoice

        // reduce the invoiceProducts same with the existing invoiceProducts.
        invoiceService.reduceInvoiceProducts(invoiceId, products);

        em.flush();
        em.clear();

        Invoice invoice = em.find(Invoice.class, invoiceId);

        // The size of the InvoiceProducts should be 10
        assertThat(invoice.getInvoiceProducts().size()).isEqualTo(10);
        // The count of the all InvoiceProducts should be 0.
        assertThat(invoice.getInvoiceProducts()).extracting(InvoiceProduct::getCount).containsExactly(0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
    }

    // Test 5 : Reduce Invoice Products that not  existing in the existing invoice
    @Test
    public void reduceInvoiceProductsNewProduct() {
        // Start of Creating One Invoice
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

        // (name, count) => (snack1, 1), (snack2, 2), ... , (snack10, 10)
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
                    ProductLevel.A);
            em.persist(product);
            Pair<Long, Integer> pair = Pair.of(product.getId(), i);
            products.add(pair);
        }
        Long invoiceId = invoiceService.invoice(martId, products);
        // End of Creating One Invoice

        // Creating pair of productId and count
        // (name, count) => (snack11, 11), (snack12, 12), ... , (snack20, 20)
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
                    ProductLevel.A);
            em.persist(product);
            Pair<Long, Integer> pair = Pair.of(product.getId(), i);
            addProducts.add(pair);
        }

        // reduce the invoiceProducts that don't exist in the InvoiceProducts of the invoice.
        // It means nothing changes.
        invoiceService.reduceInvoiceProducts(invoiceId, addProducts);

        em.flush();
        em.clear();

        Invoice invoice = em.find(Invoice.class, invoiceId);

        // The size of the invoiceProducts should be 10
        assertThat(invoice.getInvoiceProducts().size()).isEqualTo(10);
        // The count of the invoiceProducts should be same with the initial state
        assertThat(invoice.getInvoiceProducts()).extracting(InvoiceProduct::getCount).containsExactly(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

    }

    // Test 6 : Get All invoices from DB
    @Test
    void findAll() {
        // Start of Creating One Invoice
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

        // (name, count) => (snack1, 1), (snack2, 2), ... , (snack10, 10)
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
                    ProductLevel.A);
            em.persist(product);
            Pair<Long, Integer> pair = Pair.of(product.getId(), i);
            products.add(pair);
        }
        Long invoiceId = invoiceService.invoice(martId, products);
        // End of Creating One Invoice

        em.flush();
        em.clear();

        List<Invoice> invoices = invoiceService.findInvoices();

        // There is only one invoice.
        assertThat(invoices.size()).isEqualTo(1);
    }

    // Test 7 : Find One invoices that exist in the DB.
    @Test
    void findOne() {
        // Start of Creating One Invoice
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

        // (name, count) => (snack1, 1), (snack2, 2), ... , (snack10, 10)
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
                    ProductLevel.A);
            em.persist(product);
            Pair<Long, Integer> pair = Pair.of(product.getId(), i);
            products.add(pair);
        }
        Long invoiceId = invoiceService.invoice(martId, products);
        // End of Creating One Invoice

        Invoice invoice = invoiceService.findOne(invoiceId);

        // Created invoice should be found through the invoiceService
        assertThat(invoice).isNotNull();
    }

    // Test 8 : The case when finding one invoice that does not exist in the DB
    @Test
    void findOneNotExist() {
        assertThrows(IllegalArgumentException.class, () -> invoiceService.findOne(144L));
    }

    // Test 9 : Receiving invoiceProducts and update the invoice status and stockProducts of mart.
    @Test
    void receiveInvoiceProducts() {
        // Start of Creating One Invoice
        Mart mart = new Mart("mart1", "110000", "110000!", MartLevel.A);
        em.persist(mart);
        Long martId = mart.getId();


        List<StockProduct> initStockProducts = mart.getStockProducts();

        TransportCompany transportCompany = new TransportCompany("tc1");
        ProductCompany productCompany = new ProductCompany("pc1");

        em.persist(transportCompany);
        em.persist(productCompany);

        Category category = new Category("snack");

        em.persist(category);

        ArrayList<Pair<Long, Integer>> products = new ArrayList<>();

        // InvoiceProducts : (name, count) => (snack1, 1), (snack2, 2), ... , (snack10, 20)
        // StockProducts : (name, count) => (snack1, 1), (snack2, 2), ... , (snack10, 10)
        for (int i = 1; i <= 20; i++) {
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
                    ProductLevel.A);
            em.persist(product);
            Pair<Long, Integer> pair = Pair.of(product.getId(), i);
            products.add(pair);
            // adding stockProducts
            if (i <= 10) {
                em.persist(new StockProduct(i, product, mart));
            }
        }

        Long invoiceId = invoiceService.invoice(martId, products);
        // End of Creating One Invoice

        Invoice temp = em.find(Invoice.class, invoiceId);
        // It is possible to receive the products when current date is equal to or is after than arrive date
        // Change arrive date for the test
        temp.setArriveDateForTest();

        em.flush();
        em.clear();

        // Receiving InvoiceProducts
        invoiceService.receiveInvoiceProducts(martId);

        em.flush();
        em.clear();

        // Get updated mart
        mart = martService.findOne(martId);

        List<Integer> counts = mart.getStockProducts().stream().map(StockProduct::getCount).collect(Collectors.toList());
        List<Product> productList = mart.getStockProducts().stream().map(StockProduct::getProduct).collect(Collectors.toList());
        List<String> nameList = productList.stream().map(Product::getName).collect(Collectors.toList());

        // The size of the stockProducts should be 20
        assertThat(mart.getStockProducts().size()).isEqualTo(20);
        // The counts of the half of the front should be doubled, and counts of the half of the behind should be 11 ~ 20.
        assertThat(counts).containsExactly(2, 4, 6, 8, 10, 12, 14, 16, 18, 20, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20);
        // The name of the product of the stockProducts should be snack1, sncak2, ... , snack20.
        assertThat(productList).extracting(Product::getName).containsExactly(
                "snack1", "snack2", "snack3", "snack4", "snack5",
                "snack6", "snack7", "snack8", "snack9", "snack10",
                "snack11", "snack12", "snack13", "snack14", "snack15",
                "snack16", "snack17", "snack18", "snack19", "snack20"
        );

        // Check the name and count matches.
        for (int i = 1 ; i <= 20; i++) {
            if (i <= 10) {
                assertThat(counts.get(i-1)).isEqualTo(2*i);
            }
            else {
                assertThat(counts.get(i-1)).isEqualTo(i);
            }
            assertThat(nameList.get(i-1)).isEqualTo("snack" + i);
        }
    }


    // Test 10 : Cancelling the invoice
    @Test
    void cancelInvoice() {
        // Start of Creating One Invoice
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

        // (name, count) => (snack1, 1), (snack2, 2), ... , (snack10, 10)
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
                    ProductLevel.A);
            em.persist(product);
            Pair<Long, Integer> pair = Pair.of(product.getId(), i);
            products.add(pair);
        }
        Long invoiceId = invoiceService.invoice(martId, products);
        // End of Creating One Invoice

        // Change the status of the invoiceStatus to the Cancelled.
        invoiceService.cancelInvoice(invoiceId);

        em.flush();
        em.clear();

        Invoice invoice = em.find(Invoice.class, invoiceId);

        // The status of the Invoice should be Cancelled
        assertThat(invoice.getInvoiceStatus()).isEqualTo(InvoiceStatus.CANCELLED);
    }

}