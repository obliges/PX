package project.px.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.px.entity.Mart;
import project.px.repository.MartRepository;
import project.px.search.MartSearch;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class MartService {

    @PersistenceContext
    EntityManager em;

    private final MartRepository martRepository;

    public Long join(Mart m) {
        Mart savedMart = martRepository.save(m);
        return savedMart.getId();
    }

    public List<Mart> findMarts() {
        return martRepository.findAll();
    }

    public Optional<Mart> findOne(Long martId) {
        return martRepository.findById(martId);
    }

    public List<Mart> searchMarts(MartSearch martSearch) {
        return martRepository.searchMart(martSearch);
    }


}
