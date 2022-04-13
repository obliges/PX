package project.px.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.px.entity.Invoice;
import project.px.entity.InvoiceProduct;
import project.px.entity.Mart;
import project.px.entity.StockProduct;
import project.px.repository.InvoiceRepository;
import project.px.repository.MartRepository;
import project.px.repository.StockProductRepository;
import project.px.search.MartSearch;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
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

    public List<Mart> findMarts() {
        return martRepository.findAll();
    }

    public Optional<Mart> findOne(Long martId) throws NoSuchElementException {
        return martRepository.findById(martId);
    }

    public List<Mart> searchMarts(MartSearch martSearch) {
        return martRepository.searchMart(martSearch);
    }


}
