package project.px.search;

import lombok.Getter;
import project.px.entity.MartLevel;

@Getter
public class MartSearch {
    private String martName;
    private String martCode;
    private MartLevel martLevel;

    public MartSearch(String martName, String martCode, MartLevel martLevel) {
        this.martName = martName;
        this.martCode = martCode;
        this.martLevel = martLevel;
    }
}
