package project.px.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import project.px.dto.ProductDto;
import project.px.repository.ProductRepository;
import project.px.service.ProductService;

import java.util.List;

@Controller
@Slf4j
@RequestMapping("/admin/product")
@RequiredArgsConstructor
public class AdminProductController {

    private final ProductService productService;
    private final ProductRepository productRepository;

    @GetMapping
    public String productList(Model model) {
         model.addAttribute("products", productRepository.findAllDtoFetch());
         return "admin/products";
    }

}
