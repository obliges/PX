package project.px.entity;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Table(name = "transport_company")
@Getter
public class TransportCompany {
    @Id
    @GeneratedValue
    @Column(name = "transport_company_id")
    private Long id;

    private String name;
}
