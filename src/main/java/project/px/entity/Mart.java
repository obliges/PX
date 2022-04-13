package project.px.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "mart")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Mart {
    @Id
    @GeneratedValue
    @Column(name = "mart_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String martCode;

    @Column(nullable = false)
    private String passwd;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MartLevel martLevel;

    @OneToMany(mappedBy = "mart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StockProduct> stockProducts = new ArrayList<>();

    public Mart(String name, String martCode, String passwd, MartLevel martLevel) {
        this.name = name;
        this.martCode = martCode;
        this.passwd = passwd;
        this.martLevel = martLevel;
    }

    public void updateMartInfo(Mart m) {
        this.name = m.getName();
        this.martCode = m.getMartCode();
        this.passwd = m.getPasswd();
        this.martLevel = m.getMartLevel();
    }

    public void addStockProduct(StockProduct stockProduct) {
        this.stockProducts.add(stockProduct);
    }


}
