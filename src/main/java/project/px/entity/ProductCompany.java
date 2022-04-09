package project.px.entity;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Table(name = "product_company")
@Getter
public class ProductCompany {
    @Id
    @GeneratedValue
    @Column(name = "product_company_id")
    private Long id;

    private String name;
}
