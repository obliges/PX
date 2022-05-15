package project.px.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import project.px.dto.ProductCompanyDto;
import project.px.entity.ProductCompany;
import project.px.repository.ProductCompanyRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/admin/productCompany")
public class AdminProductCompanyController {

    private final ProductCompanyRepository productCompanyRepository;

    @GetMapping
    private String productCompanyList(Model model) {
        List<ProductCompany> productCompanies = productCompanyRepository.findAll();
        model.addAttribute("productCompanies", productCompanies);
        return "admin/productCompanies";
    }

    @GetMapping("/{productCompanyId}")
    public String companyInfo(@PathVariable("productCompanyId") Long productCompanyId, Model model) {
        ProductCompany productCompany = productCompanyRepository.findById(productCompanyId).orElseThrow(() -> new NoSuchElementException("Product company does not exist"));
        model.addAttribute("productCompany", productCompany);
        return "admin/productCompany";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("productCompany", new ProductCompanyDto());
        return "admin/productCompanyAddForm";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("productCompany") ProductCompanyDto productCompany, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (!StringUtils.hasText(productCompany.getName())) {
            bindingResult.rejectValue("name", "notNull", "Cannot be Null");
        }

        if (bindingResult.hasErrors()) {
            return "admin/productCompanyAddForm";
        }

        ProductCompany saveProductCompany = new ProductCompany(productCompany.getName());
        Long productCompanyId = productCompanyRepository.save(saveProductCompany).getId();
        redirectAttributes.addAttribute("productCompanyId", productCompanyId);
        return "redirect:/admin/productCompany/{productCompanyId}";
    }

    @GetMapping("/edit/{productCompanyId}")
    public String editForm(@PathVariable("productCompanyId") Long productCompanyId, Model model) {
        ProductCompany productCompany = productCompanyRepository.findById(productCompanyId).orElseThrow(() -> new NoSuchElementException("Product company does not exist"));
        ProductCompanyDto productCompanyDto = new ProductCompanyDto();
        productCompanyDto.setId(productCompany.getId());
        productCompanyDto.setName(productCompany.getName());
        model.addAttribute("productCompany", productCompanyDto);
        return "admin/productCompanyEditForm";
    }

    @PostMapping("/edit/{productCompanyId}")
    public String edit(@ModelAttribute("productCompany") ProductCompanyDto productCompanyDto, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (!StringUtils.hasText(productCompanyDto.getName())) {
            bindingResult.rejectValue("name", "notNull", "Cannot be Null");
        }

        if (bindingResult.hasErrors()) {
            return "admin/productCompanyEditForm";
        }

        ProductCompany productCompany = productCompanyRepository.findById(productCompanyDto.getId()).orElseThrow(() -> new NoSuchElementException("Product company does not exist"));;
        productCompany.DtoToObject(productCompanyDto);
        productCompanyRepository.save(productCompany);
        redirectAttributes.addAttribute("productCompanyId", productCompanyDto.getId());
        return "redirect:/admin/productCompany/{productCompanyId}";
    }
}
