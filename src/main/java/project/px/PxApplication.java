package project.px;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import project.px.entity.Mart;

import javax.persistence.EntityManager;
import javax.persistence.metamodel.SingularAttribute;

@SpringBootApplication
public class PxApplication {

	public static void main(String[] args) {
		SpringApplication.run(PxApplication.class, args);
	}

	@Bean
	JPAQueryFactory jpaQueryFactory(EntityManager em) {
		return new JPAQueryFactory(em);
	}



}
