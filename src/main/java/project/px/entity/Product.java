package project.px.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;


import javax.persistence.*;

@Entity
@Table(name = "product")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product {
    @Id
    @GeneratedValue
    @Column(name = "product_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer price;

    @Column(nullable = false)
    private Integer expirationDayPeriod;

    @Column(nullable = false)
    private Integer bigBox;

    private Integer smallBox;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_company_id", nullable = false)
    private ProductCompany productCompany;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_transport_id", nullable = false)
    private TransportCompany transportCompany;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ContractStatus contractStatus;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DemandStatus demandStatus;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductLevel productLevel;

    public Product(String name,
                   Integer price,
                   Integer expirationDayPeriod,
                   Integer bigBox,
                   Integer smallBox,
                   ProductCompany productCompany,
                   TransportCompany transportCompany,
                   Category category,
                   ContractStatus contractStatus,
                   DemandStatus demandStatus,
                   ProductLevel productLevel) {
        this.name = name;
        this.price = price;
        this.expirationDayPeriod = expirationDayPeriod;
        this.bigBox = bigBox;
        this.smallBox = smallBox;
        this.productCompany = productCompany;
        this.transportCompany = transportCompany;
        this.category = category;
        this.contractStatus = contractStatus;
        this.demandStatus = demandStatus;
        this.productLevel = productLevel;
    }
}
