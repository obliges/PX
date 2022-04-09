package project.px.entity;

import lombok.Getter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "mart")
@Getter
public class Mart {
    @Id
    @GeneratedValue
    @Column(name = "mart_id")
    private Long id;

    private Long name;

    @Enumerated(EnumType.STRING)
    private MartLevel martLevel;

    @OneToMany(mappedBy = "mart")
    private List<StockProduct> stockProducts = new ArrayList<>();



}
