package project.px.entity;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Table(name = "invoice_product")
@Getter
public class InvoiceProduct {
    @Id
    @GeneratedValue
    @Column(name = "invoice_product_id")
    private Long id;

    private Integer count;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoice_id")
    private Invoice invoice;


}
