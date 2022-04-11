package project.px.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import project.px.entity.Invoice;
import project.px.entity.QInvoice;
import project.px.entity.QInvoiceProduct;
import project.px.entity.QProduct;

import java.util.List;

import static project.px.entity.QInvoice.invoice;
import static project.px.entity.QInvoiceProduct.*;
import static project.px.entity.QProduct.*;

@RequiredArgsConstructor
public class InvoiceRepositoryImpl implements InvoiceRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Invoice findByIdFetchJoinInvoiceProductsAndProduct(Long invoiceId) {
        List<Invoice> result = queryFactory
                .selectFrom(invoice).distinct()
                .leftJoin(invoice.invoiceProducts, invoiceProduct).fetchJoin()
                .leftJoin(invoiceProduct.product, product).fetchJoin()
                .where(invoice.id.eq(invoiceId))
                .fetch();
        // Id is unique, so result only has one element or is empty(not found)
        if (result.isEmpty()) {
            return null;
        }
        else {
            return result.get(0);
        }


    }
}
