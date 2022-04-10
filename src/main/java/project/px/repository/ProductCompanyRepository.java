package project.px.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.px.entity.ProductCompany;

@Repository
public interface ProductCompanyRepository extends JpaRepository<ProductCompany, Long> {

}
