package project.px.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;


import javax.persistence.*;

@Entity
@Table(name = "product")
@Getter
@NoArgsConstructor
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_company_id")
    private ProductCompany productCompany;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_transport_id")
    private TransportCompany transportCompany;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @Enumerated(EnumType.STRING)
    private ContractStatus contractStatus;

    @Enumerated(EnumType.STRING)
    private DemandStatus demandStatus;

    @Enumerated(EnumType.STRING)
    private ProductLevel productLevel;

    public Product(String name) {
        this.name = name;
    }

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
