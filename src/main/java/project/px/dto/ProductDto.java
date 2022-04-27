package project.px.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.Setter;
import project.px.entity.*;

import javax.persistence.*;

@Getter
@Setter
public class ProductDto {
    private Long id;

    private String name;

    private Integer price;

    private Integer expirationDayPeriod;

    private Integer bigBox;

    private Integer smallBox;

    private ProductCompany productCompany;

    private TransportCompany transportCompany;

    private Category category;

    private ContractStatus contractStatus;

    private DemandStatus demandStatus;

    private ProductLevel productLevel;

    @QueryProjection
    public ProductDto(Long id, String name, Integer price, Integer expirationDayPeriod, Integer bigBox, Integer smallBox, ProductCompany productCompany, TransportCompany transportCompany, Category category, ContractStatus contractStatus, DemandStatus demandStatus, ProductLevel productLevel) {
        this.id = id;
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
