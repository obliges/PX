package project.px.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.px.entity.Mart;
import project.px.repository.MartRepository;
import project.px.search.MartSearch;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class MartService {


    private final MartRepository martRepository;

    public Long join(Mart m) {
        Mart savedMart = martRepository.save(m);
        return savedMart.getId();
    }

    @Transactional(readOnly = true)
    public List<Mart> findMarts() {
        return martRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Mart> findOne(Long martId) {
        return martRepository.findById(martId);
    }

    // Get all marts that satisfy the Conditions of the martSearch
    // Conditions : MartName, MartCode, MartLevel
    @Transactional(readOnly = true)
    public List<Mart> searchMarts(MartSearch martSearch) {
        return martRepository.searchMart(martSearch);
    }


}
