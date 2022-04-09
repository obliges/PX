package project.px.entity;

import javax.persistence.*;

@Entity
@Table(name = "transport_company")
public class TransportCompany {
    @Id
    @GeneratedValue
    @Column(name = "transport_company_id")
    private Long id;

    private String name;
}
