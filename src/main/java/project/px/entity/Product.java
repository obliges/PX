package project.px.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "product")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Product {
    @Id
    @GeneratedValue
    @Column(name = "product_id")
    private Long id;

    private String name;

    private Integer price;

    private Integer expirationDayPeriod;

    private Integer bigBox;

    private Integer smallBox;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_company_id")
    private ProductCompany productCompany;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_transport_id")
    private TransportCompany transportCompany;

    @Enumerated(EnumType.STRING)
    private ContractStatus contractStatus;

    @Enumerated(EnumType.STRING)
    private DemandStatus demandStatus;


    public Product(String name, Integer price, Integer expirationDayPeriod, Integer bigBox, Integer smallBox) {
        this.name = name;
        this.price = price;
        this.expirationDayPeriod = expirationDayPeriod;
        this.bigBox = bigBox;
        this.smallBox = smallBox;
    }
}