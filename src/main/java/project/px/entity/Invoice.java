package project.px.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "invoice")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Invoice {
    @Id
    @GeneratedValue
    @Column(name = "invoice_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mart_id", nullable = false)
    private Mart mart;

    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL)
    private List<InvoiceProduct> invoiceProducts = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InvoiceStatus invoiceStatus;

    @Column(nullable = false)
    private LocalDate arriveDate;

    // private Setter
    private void setMart(Mart mart) {
        this.mart = mart;
    }


    private void setInvoiceStatus(InvoiceStatus invoiceStatus) {
        this.invoiceStatus = invoiceStatus;
    }

    private void setArriveDate(LocalDate arriveDate) {
        this.arriveDate = arriveDate;
    }

    // method
    public static Invoice createInvoice(Mart mart, List<InvoiceProduct> invoiceProducts) {
        Invoice invoice = new Invoice();
        invoice.setMart(mart);
        invoiceProducts.forEach(invoice::addInvoiceProduct);
        invoice.setInvoiceStatus(project.px.entity.InvoiceStatus.DELIVERING);
        invoice.setArriveDate(LocalDate.now().plusDays(3));
        // Later, consider the case when client create invoice 23:59:59 and server handles it after next day of 00:00:00.
        return invoice;
    }

    public void addInvoiceProduct(InvoiceProduct invoiceProduct) {
        invoiceProduct.setInvoice(this);
        this.invoiceProducts.add(invoiceProduct);
    }

    public void deliveryArrived() {
        this.setInvoiceStatus(InvoiceStatus.ARRIVED);
    }

    public void invoiceCancelled() {
        this.setInvoiceStatus(InvoiceStatus.CANCELLED);
    }

    public void setArriveDateForTest() {
        this.arriveDate = LocalDate.of(1000, 1, 1);
    }
}
