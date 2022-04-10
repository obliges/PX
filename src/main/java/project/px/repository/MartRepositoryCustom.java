package project.px.repository;

import org.springframework.stereotype.Repository;
import project.px.entity.Mart;
import project.px.search.MartSearch;

import java.util.List;

@Repository
public interface MartRepositoryCustom {
    List<Mart> searchMart(MartSearch martSearch);
}
