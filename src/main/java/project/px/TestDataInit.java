package project.px;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import project.px.entity.Category;
import project.px.entity.ProductCompany;
import project.px.entity.TransportCompany;
import project.px.repository.CategoryRepository;
import project.px.repository.ProductCompanyRepository;
import project.px.repository.TransportCompanyRepository;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Component
@RequiredArgsConstructor
public class TestDataInit {

    private final ProductCompanyRepository productCompanyRepository;
    private final TransportCompanyRepository transportCompanyRepository;
    private final CategoryRepository categoryRepository;

    @PostConstruct
    public void init() {
        for (int i = 1; i <= 10; i++) {
            ProductCompany productCompany = new ProductCompany("pc" + i);
            TransportCompany transportCompany = new TransportCompany("tc" + i);
            Category category = new Category("category" + i);
            productCompanyRepository.save(productCompany);
            transportCompanyRepository.save(transportCompany);
            categoryRepository.save(category);
        }
    }
}
