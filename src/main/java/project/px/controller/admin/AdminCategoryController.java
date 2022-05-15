package project.px.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import project.px.dto.CategoryDto;
import project.px.entity.Category;
import project.px.repository.CategoryRepository;

import java.util.List;
import java.util.NoSuchElementException;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/admin/category")
public class AdminCategoryController {

    private final CategoryRepository categoryRepository;

    @GetMapping
    private String categoryList(Model model) {
        List<Category> categories = categoryRepository.findAll();
        model.addAttribute("categories", categories);
        return "admin/categories";
    }

    @GetMapping("/{categoryId}")
    public String categoryInfo(@PathVariable("categoryId") Long categoryId, Model model) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new NoSuchElementException("Category does not exist"));
        model.addAttribute("category", category);
        return "admin/category";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("category", new CategoryDto());
        return "admin/categoryAddForm";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("category") CategoryDto category, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (!StringUtils.hasText(category.getName())) {
            bindingResult.rejectValue("name", "notNull", "Cannot be Null");
        }

        if (bindingResult.hasErrors()) {
            return "admin/categoryAddForm";
        }

        Category saveCategory = new Category(category.getName());
        Long categoryId = categoryRepository.save(saveCategory).getId();
        redirectAttributes.addAttribute("categoryId", categoryId);
        return "redirect:/admin/category/{categoryId}";
    }

    @GetMapping("/edit/{categoryId}")
    public String editForm(@PathVariable("categoryId") Long categoryId, Model model) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new NoSuchElementException("Category does not exist"));
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(category.getId());
        categoryDto.setName(category.getName());
        model.addAttribute("category", categoryDto);
        return "admin/categoryEditForm";
    }

    @PostMapping("/edit/{categoryId}")
    public String edit(@ModelAttribute("category") CategoryDto categoryDto, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (!StringUtils.hasText(categoryDto.getName())) {
            bindingResult.rejectValue("name", "notNull", "Cannot be Null");
        }

        if (bindingResult.hasErrors()) {
            return "admin/categoryEditForm";
        }

        Category category = categoryRepository.findById(categoryDto.getId()).orElseThrow(() -> new NoSuchElementException("Category does not exist"));;
        category.DtoToObject(categoryDto);
        categoryRepository.save(category);
        redirectAttributes.addAttribute("categoryId", categoryDto.getId());
        return "redirect:/admin/category/{categoryId}";
    }
}
