package project.px.entity;

import com.sun.istack.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;


@Entity
@Table(name = "transport_company")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TransportCompany {
    @Id
    @GeneratedValue
    @Column(name = "transport_company_id")
    private Long id;

    private String name;

    public TransportCompany(String name) {
        this.name = name;
    }
}
