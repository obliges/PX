package project.px.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import project.px.dto.ProductAddForm;
import project.px.dto.ProductDto;
import project.px.entity.*;
import project.px.repository.ProductRepository;
import project.px.service.ProductService;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
        List<ProductDto> products = productRepository.findAllFetch()
                .stream().map(Product::productToDto)
                .collect(Collectors.toList());
        model.addAttribute("products", products);
        return "admin/products";
    }

    @GetMapping("/{productId}")
    public String productInfo(@PathVariable("productId") Long productId, Model model) {
        ProductDto product = Product.productToDto(productService.findOne(productId));
        model.addAttribute("product", product);
        return "admin/product";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("product", new Product());
        return "admin/productAddForm";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("product") @Validated ProductAddForm form, BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        if (form.getSmallBox() != null) {
            if (form.getBigBox() != null && form.getBigBox() <= form.getSmallBox()) {
                bindingResult.reject("bigSmallBox", "SmallBox should be smaller than BigBox.");
            }
        }

        if (bindingResult.hasErrors()) {
            return "admin/productAddForm";
        }

        Long productId = productService.add(Product.dtoToProduct(form));
        redirectAttributes.addAttribute("productId", productId);
        return "redirect:/admin/product/{productId}";
    }

    @GetMapping("/edit/{productId}")
    public String editForm(@PathVariable("productId") Long productId, Model model) {
        ProductDto product = Product.productToDto(productService.findOne(productId));
        model.addAttribute("product", product);
        return "admin/productEditForm";
    }
}