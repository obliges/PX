package project.px.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import project.px.dto.TransportCompanyDto;
import project.px.entity.TransportCompany;
import project.px.repository.TransportCompanyRepository;

import java.util.List;
import java.util.NoSuchElementException;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/admin/transportCompany")
public class AdminTransportCompanyController {

    private final TransportCompanyRepository transportCompanyRepository;

    @GetMapping
    private String transportCompanyList(Model model) {
        List<TransportCompany> transportCompanies = transportCompanyRepository.findAll();
        model.addAttribute("transportCompanies", transportCompanies);
        return "admin/transportCompanies";
    }

    @GetMapping("/{transportCompanyId}")
    public String companyInfo(@PathVariable("transportCompanyId") Long transportCompanyId, Model model) {
        TransportCompany transportCompany = transportCompanyRepository.findById(transportCompanyId).orElseThrow(() -> new NoSuchElementException("Transport company does not exist"));
        model.addAttribute("transportCompany", transportCompany);
        return "admin/transportCompany";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("transportCompany", new TransportCompanyDto());
        return "admin/transportCompanyAddForm";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("transportCompany") TransportCompanyDto transportCompany, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (!StringUtils.hasText(transportCompany.getName())) {
            bindingResult.rejectValue("name", "notNull", "Cannot be Null");
        }

        if (bindingResult.hasErrors()) {
            return "admin/transportCompanyAddForm";
        }

        TransportCompany saveTransportCompany = new TransportCompany(transportCompany.getName());
        Long transportCompanyId = transportCompanyRepository.save(saveTransportCompany).getId();
        redirectAttributes.addAttribute("transportCompanyId", transportCompanyId);
        return "redirect:/admin/transportCompany/{transportCompanyId}";
    }

    @GetMapping("/edit/{transportCompanyId}")
    public String editForm(@PathVariable("transportCompanyId") Long transportCompanyId, Model model) {
        TransportCompany transportCompany = transportCompanyRepository.findById(transportCompanyId).orElseThrow(() -> new NoSuchElementException("Transport company does not exist"));
        TransportCompanyDto transportCompanyDto = new TransportCompanyDto();
        transportCompanyDto.setId(transportCompany.getId());
        transportCompanyDto.setName(transportCompany.getName());
        model.addAttribute("transportCompany", transportCompanyDto);
        return "admin/transportCompanyEditForm";
    }

    @PostMapping("/edit/{transportCompanyId}")
    public String edit(@ModelAttribute("transportCompany") TransportCompanyDto transportCompanyDto, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (!StringUtils.hasText(transportCompanyDto.getName())) {
            bindingResult.rejectValue("name", "notNull", "Cannot be Null");
        }

        if (bindingResult.hasErrors()) {
            return "admin/transportCompanyEditForm";
        }

        TransportCompany transportCompany = transportCompanyRepository.findById(transportCompanyDto.getId()).orElseThrow(() -> new NoSuchElementException("Transport company does not exist"));;
        transportCompany.DtoToObject(transportCompanyDto);
        transportCompanyRepository.save(transportCompany);
        redirectAttributes.addAttribute("transportCompanyId", transportCompanyDto.getId());
        return "redirect:/admin/transportCompany/{transportCompanyId}";
    }
}
