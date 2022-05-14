package project.px.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import project.px.dto.ProductEditForm;
import project.px.entity.*;

import javax.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ProductServiceTest {

    @Autowired
    EntityManager em;

    @Autowired
    ProductService productService;


    // Test 1 : Add product to DB
    @Test
    public void add() {
        Product product = new Product("snack");
        Long productId = productService.add(product);

        em.flush();
        em.clear();

        Product foundProduct = em.find(Product.class, productId);

        // Id should be same
        assertThat(foundProduct.getId()).isEqualTo(productId);
        // The name of the added product should be snack
        assertThat(foundProduct.getName()).isEqualTo("snack");
    }

    // Test 2 : Find all product from DB
    @Test
    public void findProducts() {
        for (int i = 1; i <= 10; i++) {
            Product product = new Product("snack" + i);
            em.persist(product);
        }

        em.flush();
        em.clear();

        List<Product> products = productService.findProducts();

        // The size of the products should be 10
        assertThat(products.size()).isEqualTo(10);
        // The name of the products should be snack1, snack2, ... , snack10
        assertThat(products).extracting(Product::getName).containsExactly(
                "snack1", "snack2", "snack3", "snack4", "snack5",
                "snack6", "snack7", "snack8", "snack9", "snack10"
        );
    }

    // Test 3 : Find one that exists in DB
    @Test
    public void findOneExist() {
        Product product = new Product("snack");
        Long productId = productService.add(product);

        em.flush();
        em.clear();

        Product foundProduct = productService.findOne(productId);

        // The name of the foundProduct should be snack
        assertThat(foundProduct.getName()).isEqualTo("snack");
    }

    // Test 4 : Find One that does not exist in DB
    @Test
    public void findOneNotExist() {
        assertThrows(IllegalArgumentException.class, () -> productService.findOne(16579L));
    }

    // Test 5 : Update product using editForm
    @Test
    public void update() {
        Product product = new Product("snack");
        Long productId = productService.add(product);

        ProductEditForm form = new ProductEditForm();
        form.setName("juice");
        product.update(form);

        em.flush();
        em.clear();

        Product foundProduct = productService.findOne(productId);
        assertThat(foundProduct.getName()).isEqualTo("juice");
    }


}