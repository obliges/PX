package project.px.entity;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Table(name = "category")
@Getter
public class Category {
    @Id
    @GeneratedValue
    @Column(name = "category_id")
    private Long id;

    private String name;

}
