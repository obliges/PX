package project.px.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.px.entity.StockProduct;

@Repository
public interface StockProductRepository extends JpaRepository<StockProduct, Long> {

}
