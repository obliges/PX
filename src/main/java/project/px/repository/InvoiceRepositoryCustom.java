package project.px.repository;

import org.springframework.stereotype.Repository;
import project.px.entity.Invoice;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface InvoiceRepositoryCustom {

    Invoice findByIdFetchJoinInvoiceProductsAndProduct(Long invoiceId);

    List<Invoice> findInvoicesForReceiveInvoiceProducts(Long martId);
}
