package project.px.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import project.px.entity.*;

import java.time.LocalDate;
import java.util.List;

import static project.px.entity.QInvoice.invoice;
import static project.px.entity.QInvoiceProduct.*;
import static project.px.entity.QMart.*;
import static project.px.entity.QProduct.*;
import static project.px.entity.QStockProduct.*;

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

    @Override
    public List<Invoice> findInvoicesForReceiveInvoiceProducts(Long martId) {
        List<Invoice> result = queryFactory
                .selectFrom(invoice).distinct()
                .leftJoin(invoice.invoiceProducts, invoiceProduct).fetchJoin()
                .leftJoin(invoiceProduct.product, product).fetchJoin()
                .leftJoin(invoice.mart, mart).fetchJoin()
                .leftJoin(invoice.mart.stockProducts, stockProduct).fetchJoin()
                .where(invoice.mart.id.eq(martId))
                .fetch();
        return result;
    }
}
