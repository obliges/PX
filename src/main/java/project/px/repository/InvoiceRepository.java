package project.px.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.px.entity.Invoice;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

}
