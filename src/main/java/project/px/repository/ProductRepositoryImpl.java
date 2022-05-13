package project.px.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import project.px.entity.Product;

import java.util.List;

import static project.px.entity.QProduct.product;

@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepositoryCustom {

    private final JPAQueryFactory queryFactory;


    @Override
    public List<Product> findAllFetch() {
        return queryFactory.selectFrom(product)
                .leftJoin(product.productCompany).fetchJoin()
                .leftJoin(product.transportCompany).fetchJoin()
                .leftJoin(product.category).fetchJoin()
                .fetch();
    }
}
