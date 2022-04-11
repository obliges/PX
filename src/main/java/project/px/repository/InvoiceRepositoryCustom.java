package project.px.repository;

import org.springframework.stereotype.Repository;
import project.px.entity.Invoice;

@Repository
public interface InvoiceRepositoryCustom {

    Invoice findByIdFetchJoinInvoiceProductsAndProduct(Long invoiceId);
}
