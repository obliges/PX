package project.px.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import project.px.entity.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
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


}
