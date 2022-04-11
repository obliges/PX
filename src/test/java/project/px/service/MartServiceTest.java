package project.px.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import project.px.entity.Mart;
import project.px.entity.MartLevel;
import project.px.repository.MartRepository;
import project.px.search.MartSearch;

import javax.persistence.EntityManager;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MartServiceTest {

    @Autowired
    EntityManager em;

    @Autowired
    MartService martService;

    @Test
    public void join() {
        Mart mart1 = new Mart("mart1", "110000", "110000!", MartLevel.A);
        Mart mart2 = new Mart("mart2", "110001", "110001!", MartLevel.B);

        Long id1 = martService.join(mart1);
        Long id2 = martService.join(mart2);

        assertThat(mart1.getId()).isEqualTo(id1);
        assertThat(mart2.getId()).isEqualTo(id2);
    }

    @Test
    public void joinAlreadyExist() {
        Mart mart = new Mart("mart1", "110000", "110000!", MartLevel.A);
        martService.join(mart);
        Mart findMart = em.find(Mart.class, mart.getId());

        findMart.updateMartInfo(new Mart("mart2", "110001", "110001!", MartLevel.B));
        martService.join(findMart);
        List<Mart> findMarts = em.createQuery("select m from Mart m where m.id = :id", Mart.class)
                .setParameter("id", mart.getId())
                .getResultList();

        assertThat(findMarts.size()).isEqualTo(1);
        assertThat(findMarts.get(0).getName()).isEqualTo("mart2");
        assertThat(findMarts.get(0)).isEqualTo(findMart);
    }

    @Test
    public void findAll() {
        Mart mart1 = new Mart("mart1", "110000", "110000!", MartLevel.A);
        Mart mart2 = new Mart("mart2", "110001", "110001!", MartLevel.B);
        em.persist(mart1);
        em.persist(mart2);

        List<Mart> result = martService.findMarts();

        assertThat(result.size()).isEqualTo(2);
        assertThat(result).extracting(Mart::getName).containsExactly("mart1", "mart2");
    }

    @Test
    public void findAllEmpty() {
        List<Mart> result = martService.findMarts();

        assertThat(result.size()).isEqualTo(0);
    }

    @Test
    public void findOne() {
        Mart mart1 = new Mart("mart1", "110000", "110000!", MartLevel.A);
        em.persist(mart1);

        Long mart1_id = mart1.getId();

        em.flush();
        em.clear();

        Optional<Mart> mart2 = martService.findOne(mart1_id);
        assertThat(mart2.get().getId()).isEqualTo(mart1_id);
        assertThat(mart2.get()).isEqualTo(mart1);
    }

    @Test
    public void findOneNotExist() {
        Optional<Mart> mart = martService.findOne(123L);
        assertThat(mart.isPresent()).isEqualTo(false);
        assertThat(mart.isEmpty()).isEqualTo(true);
    }

    @Test
    public void searchMart() {
        for (int i = 1; i <= 10; i++) {
            em.persist(new Mart("mart" + i, "1100" + i, "154894", i % 2 == 0 ? MartLevel.A : MartLevel.B));
        }

        String martName = "art";
        String martCode = "11001";
        MartLevel martLevel = null;

        List<Mart> result1 = martService.searchMarts(new MartSearch(martName, martCode, martLevel));
        assertThat(result1.size()).isEqualTo(2);
        assertThat(result1).extracting(Mart::getName).containsExactly("mart1", "mart10");

        List<Mart> result2 = martService.searchMarts(new MartSearch(null, "11", MartLevel.B));
        assertThat(result2.size()).isEqualTo(5);
        assertThat(result2).extracting(Mart::getName).containsExactly("mart1", "mart3", "mart5", "mart7", "mart9");

    }
}