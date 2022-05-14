package project.px.entity;


import lombok.*;
import project.px.dto.ProductCompanyDto;

import javax.persistence.*;



@Entity
@Table(name = "product_company")
@Getter
@NoArgsConstructor
public class ProductCompany {
    @Id
    @GeneratedValue
    @Column(name = "product_company_id")
    private Long id;

    private String name;

    public ProductCompany(String name) {
        this.name = name;
    }

    public void DtoToObject(ProductCompanyDto productCompanyDto) {
        this.id = productCompanyDto.getId();
        this.name = productCompanyDto.getName();;
    }
}
