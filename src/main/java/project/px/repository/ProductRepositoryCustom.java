package project.px.repository;

import org.springframework.stereotype.Repository;
import project.px.dto.ProductDto;

import java.util.List;

@Repository
public interface ProductRepositoryCustom {

    List<ProductDto> findAllDtoFetch();

}
