package project.px.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.px.entity.Mart;

@Repository
public interface MartRepository extends JpaRepository<Mart, Long>, MartRepositoryCustom {


}
