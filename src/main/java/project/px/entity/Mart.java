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

    private String name;

    //make unique
    private String martCode;

    private String passwd;

    @Enumerated(EnumType.STRING)
    private MartLevel martLevel;

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


}
