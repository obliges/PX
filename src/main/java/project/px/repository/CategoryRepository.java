package project.px.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.px.entity.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

}
