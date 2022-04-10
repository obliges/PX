package project.px.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.px.entity.InvoiceProduct;

@Repository
public interface InvoiceProductRepository extends JpaRepository<InvoiceProduct, Long> {

}
