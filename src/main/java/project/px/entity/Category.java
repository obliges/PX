package project.px.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;
import project.px.dto.CategoryDto;
import project.px.dto.TransportCompanyDto;

import javax.persistence.*;

@Entity
@Table(name = "category")
@Getter
@NoArgsConstructor
public class Category {
    @Id
    @GeneratedValue
    @Column(name = "category_id")
    private Long id;

    private String name;

    public Category(String name) {
        this.name = name;
    }

    public void DtoToObject(CategoryDto categoryDto) {
        this.id = categoryDto.getId();
        this.name = categoryDto.getName();;
    }
}
