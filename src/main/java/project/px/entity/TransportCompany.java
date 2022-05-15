package project.px.entity;

import com.sun.istack.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import project.px.dto.ProductCompanyDto;
import project.px.dto.TransportCompanyDto;

import javax.persistence.*;


@Entity
@Table(name = "transport_company")
@Getter
@NoArgsConstructor
public class TransportCompany {
    @Id
    @GeneratedValue
    @Column(name = "transport_company_id")
    private Long id;

    private String name;

    public TransportCompany(String name) {
        this.name = name;
    }

    public void DtoToObject(TransportCompanyDto transportCompanyDto) {
        this.id = transportCompanyDto.getId();
        this.name = transportCompanyDto.getName();;
    }
}
