package project.px.repository;

import org.springframework.stereotype.Repository;
import project.px.entity.Product;

import java.util.List;

@Repository
public interface ProductRepositoryCustom {

    List<Product> findAllFetch();

}
