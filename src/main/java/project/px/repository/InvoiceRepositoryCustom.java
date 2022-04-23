package project.px.repository;

import org.springframework.stereotype.Repository;
import project.px.entity.Invoice;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface InvoiceRepositoryCustom {

    Optional<Invoice> findByIdFetchJoinInvoiceProductsAndProduct(Long invoiceId);

    List<Invoice> findInvoicesForReceiveInvoiceProducts(Long martId);
}
