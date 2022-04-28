package project.px.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Entity
@Table(name = "stock_product")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StockProduct {
    @Id
    @GeneratedValue
    @Column(name = "stock_product_id")
    private Long id;

    private Integer count;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mart_id")
    private Mart mart;

    public StockProduct(Integer count, Product product, Mart mart) {
        this.count = count;
        this.product = product;
        this.mart = mart;
        mart.addStockProduct(this);
    }

    public void addCount(Integer add) {
        this.count += add;
    }
}
