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

    // Test 1 : Join new Mart
    @Test
    public void join() {
        Mart mart1 = new Mart("mart1");
        Mart mart2 = new Mart("mart2");

        Long id1 = martService.join(mart1);
        Long id2 = martService.join(mart2);

        em.flush();
        em.clear();

        List<Mart> marts = em.createQuery("select m from Mart m", Mart.class)
                .getResultList();

        // The number of marts in DB should be 2
        assertThat(marts.size()).isEqualTo(2);
        // The each name of marts in DB should be mart1, mart2
        assertThat(marts).extracting(Mart::getName).containsExactly("mart1", "mart2");
    }

    // Test 2 : Join existing Mart (currently it is merged)
    @Test
    public void joinAlreadyExist() {
        Mart mart = new Mart("mart1");
        martService.join(mart);

        mart.updateMartInfo(new Mart("mart2"));
        martService.join(mart); // merged with existing mart

        em.flush();
        em.clear();

        List<Mart> findMarts = em.createQuery("select m from Mart m where m.id = :id", Mart.class)
                .setParameter("id", mart.getId())
                .getResultList();

        // The number of marts in DB should be 1
        assertThat(findMarts.size()).isEqualTo(1);
        // The existing mart's name should be changed into mart2
        assertThat(findMarts.get(0).getName()).isEqualTo("mart2");
    }

    // Test 3 : Find all marts from DB
    @Test
    public void findAll() {
        Mart mart1 = new Mart("mart1");
        Mart mart2 = new Mart("mart2");
        em.persist(mart1);
        em.persist(mart2);

        em.flush();
        em.clear();

        List<Mart> result = martService.findMarts();

        // The number of marts in DB should be 2
        assertThat(result.size()).isEqualTo(2);
        // The each name of marts in DB should be mart1, mart2
        assertThat(result).extracting(Mart::getName).containsExactly("mart1", "mart2");
    }

    // Test 4 : find all marts from DB that does not have any marts
    @Test
    public void findAllEmpty() {
        List<Mart> result = martService.findMarts();

        // The number of marts in DB should be 0 (Empty)
        assertThat(result.size()).isEqualTo(0);
    }

    // Test 5 : Find one mart from DB
    @Test
    public void findOne() {
        Mart mart = new Mart("mart1");
        em.persist(mart);

        Long mart_id = mart.getId();

        em.flush();
        em.clear();

        Mart foundMart = martService.findOne(mart_id);

        // mart should not be null (found)
        assertThat(foundMart.getName()).isEqualTo("mart1");
    }

    // Test 6 : Find one mart that is not exist in the DB
    @Test
    public void findOneNotExist() {
        assertThrows(IllegalArgumentException.class, () -> martService.findOne(123L));
    }

    // Test 7 : Find all marts that satisfy the some conditions
    @Test
    public void searchMart() {
        /*
        mart1, 11001, 154894, MartLevel.B
        mart2, 11002, 154894, MartLevel.A
        ...
        ...
        mart10, 110010, 154894, MartLevel.A
         */
        for (int i = 1; i <= 10; i++) {
            em.persist(new Mart("mart" + i, "1100" + i, "154894", i % 2 == 0 ? MartLevel.A : MartLevel.B));
        }

        // Case 1 : martName contains art, martCode contains 11001
        List<Mart> result1 = martService.searchMarts(new MartSearch("art", "11001", null));
        assertThat(result1.size()).isEqualTo(2);
        assertThat(result1).extracting(Mart::getName).containsExactly("mart1", "mart10");

        // Case 2 : martCode contains 11, martLevel is B
        List<Mart> result2 = martService.searchMarts(new MartSearch(null, "11", MartLevel.B));
        assertThat(result2.size()).isEqualTo(5);
        assertThat(result2).extracting(Mart::getName).containsExactly("mart1", "mart3", "mart5", "mart7", "mart9");

    }
}