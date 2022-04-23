package project.px.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "invoice_product")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class InvoiceProduct {
    @Id
    @GeneratedValue
    @Column(name = "invoice_product_id")
    private Long id;

    @Column(nullable = false)
    private Integer count;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoice_id", nullable = false)
    private Invoice invoice;

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    public InvoiceProduct(Integer count, Product product) {
        this.count = count;
        this.product = product;
    }

    public InvoiceProduct(Integer count, Product product, Invoice invoice) {
        this.count = count;
        this.product = product;
        this.invoice = invoice;
        invoice.addInvoiceProduct(this);
    }

    public void addCount(Integer add) {
        if (add < 0) {
            throw new IllegalArgumentException("amount should not be minus");
        }
        this.count += add;
    }

    public void reduceCount(Integer remove) {
        if (this.count - remove < 0) {
            throw new IllegalArgumentException("count should not be minus");
        }
        this.count -= remove;
    }
}
