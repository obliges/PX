package project.px.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import project.px.entity.*;
import project.px.repository.ProductRepository;
import project.px.service.ProductService;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

@Controller
@Slf4j
@RequestMapping("/admin/product")
@RequiredArgsConstructor
public class AdminProductController {

    private final ProductService productService;
    private final ProductRepository productRepository;

    @PersistenceContext
    EntityManager em;

    @ModelAttribute("productCompanies")
    public List<ProductCompany> productCompanies() {
        List<ProductCompany> productCompanies = new ArrayList<>();
        em.createQuery("select pc from ProductCompany pc", ProductCompany.class)
                .getResultList()
                .forEach(pc -> productCompanies.add(pc));
        return productCompanies;
    }

    @ModelAttribute("transportCompanies")
    public List<TransportCompany> transportCompanies() {
        List<TransportCompany> transportCompanies = new ArrayList<>();
        em.createQuery("select tc from TransportCompany tc", TransportCompany.class)
                .getResultList()
                .forEach(tc -> transportCompanies.add(tc));
        return transportCompanies;
    }

    @ModelAttribute("categories")
    public List<Category> categories() {
        List<Category> categories = new ArrayList<>();
        em.createQuery("select c from Category c", Category.class)
                .getResultList()
                .forEach(c -> categories.add(c));
        return categories;
    }

    @ModelAttribute("contractStatusList")
    public ContractStatus[] contractStatusList() {
        return ContractStatus.values();
    }

    @ModelAttribute("demandStatusList")
    public DemandStatus[] demandStatusList() {
        return DemandStatus.values();
    }

    @ModelAttribute("productLevels")
    public ProductLevel[] productLevels() {
        return ProductLevel.values();
    }


    @GetMapping
    public String productList(Model model) {
        model.addAttribute("products", new Product());
        return "admin/products";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("product", new Product());
        return "admin/productAddForm";
    }
}