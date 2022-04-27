package project.px.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import project.px.dto.ProductDto;
import project.px.dto.QProductDto;
import project.px.entity.QProduct;

import java.util.List;

import static project.px.entity.QProduct.product;

@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<ProductDto> findAllDtoFetch() {
        List<ProductDto> result = queryFactory.select(new QProductDto(
                product.id,
                product.name,
                product.price,
                product.expirationDayPeriod,
                product.bigBox,
                product.smallBox,
                product.productCompany,
                product.transportCompany,
                product.category,
                product.contractStatus,
                product.demandStatus,
                product.productLevel))
                .from(product)
                .join(product.productCompany)
                .join(product.transportCompany)
                .join(product.category)
                .fetch();
        return result;
    }
}
