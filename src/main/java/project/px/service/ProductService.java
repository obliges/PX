package project.px.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.px.dto.ProductEditForm;
import project.px.entity.Product;
import project.px.repository.ProductRepository;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public Long add(Product product) {
        Product savedProduct = productRepository.save(product);
        return savedProduct.getId();
    }

    @Transactional(readOnly = true)
    public List<Product> findProducts() {
        return productRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Product findOne(Long productId) {
        return productRepository.findById(productId).orElseThrow(
                () -> new IllegalArgumentException("Product whose id is " + productId + " does not exist.")
        );
    }

    public void update(Product product, ProductEditForm form) {
        product.update(form);
    }
}
