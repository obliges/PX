package project.px.entity;

import javax.persistence.*;

@Entity
@Table(name = "product_company")
public class ProductCompany {
    @Id
    @GeneratedValue
    @Column(name = "product_company_id")
    private Long id;

    private String name;
}
