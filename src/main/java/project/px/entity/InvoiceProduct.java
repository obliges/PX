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
        if (this.invoice == null) {
            this.invoice = invoice;
        }
        else {
            //Maybe throws exception
        }
    }

    public InvoiceProduct(Integer count, Product product) {
        this.count = count;
        this.product = product;
    }

    public void addCount(Integer add) {
        this.count += add;
    }

    public void reduceCount(Integer remove) {
        if (this.count - remove < 0) {
            return;
        }
        this.count -= remove;
    }
}
