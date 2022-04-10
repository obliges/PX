package project.px.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;
import project.px.entity.Mart;
import project.px.entity.MartLevel;
import project.px.search.MartSearch;

import java.util.List;

import static project.px.entity.QMart.*;

@RequiredArgsConstructor
public class MartRepositoryImpl implements MartRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Mart> searchMart(MartSearch martSearch) {
        return jpaQueryFactory
                .selectFrom(mart)
                .where(
                        martNameContain(martSearch.getMartName()),
                        martCodeContain(martSearch.getMartCode()),
                        martLevelEq(martSearch.getMartLevel())
                )
                .fetch();
    }

    private BooleanExpression martLevelEq(MartLevel martLevel) {
        return martLevel != null ? mart.martLevel.eq(martLevel) : null;
    }

    private BooleanExpression martCodeContain(String martCode) {
        return StringUtils.hasText(martCode) ? mart.martCode.contains(martCode) : null;
    }

    private BooleanExpression martNameContain(String martName) {
        return StringUtils.hasText(martName) ? mart.name.contains(martName) : null;
    }
}
