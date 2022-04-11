package project.px.entity;

import lombok.Getter;

import javax.persistence.*;


@Entity
@Table(name = "stock_product")
@Getter
public class StockProduct {
    @Id
    @GeneratedValue
    @Column(name = "stock_product_id")
    private Long id;

    @Column(nullable = false)
    private Integer count;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mart_id", nullable = false)
    private Mart mart;
}
