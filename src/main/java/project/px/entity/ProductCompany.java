package project.px.entity;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;



@Entity
@Table(name = "product_company")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductCompany {
    @Id
    @GeneratedValue
    @Column(name = "product_company_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    public ProductCompany(String name) {
        this.name = name;
    }
}
