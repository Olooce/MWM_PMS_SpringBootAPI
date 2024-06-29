package oloo.mwm_pms.controllers;

import oloo.mwm_pms.entinties.Salary;
import oloo.mwm_pms.repositories.SalaryRepository;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilderFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/salaries")
public class SalaryController {

    private final SalaryRepository salaryRepository;

    public SalaryController(SalaryRepository salaryRepository) {
        this.salaryRepository = salaryRepository;
    }

    @GetMapping
    public PagedModel<Salary> getAllSalaries(@RequestParam(defaultValue = "0") int page,
                                             @RequestParam(defaultValue = "10") int size) {
        List<Salary> salaries = salaryRepository.findAll(page, size);
        int totalSalaries = salaryRepository.count();
        Pageable pageable = PageRequest.of(page, size);
        PageImpl<Salary> salaryPage = new PageImpl<>(salaries, pageable, totalSalaries);

        PagedModel.PageMetadata pageMetadata = new PagedModel.PageMetadata(size, page, totalSalaries);
        WebMvcLinkBuilderFactory factory = new WebMvcLinkBuilderFactory();
        WebMvcLinkBuilder linkBuilder = factory.linkTo(WebMvcLinkBuilder.methodOn(SalaryController.class).getAllSalaries(page, size));
        return PagedModel.of(salaries, pageMetadata, linkBuilder.withSelfRel());
    }
}
