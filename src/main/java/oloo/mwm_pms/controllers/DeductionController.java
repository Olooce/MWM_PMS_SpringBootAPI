package oloo.mwm_pms.controllers;

import oloo.mwm_pms.entinties.Deduction;
import oloo.mwm_pms.repositories.DeductionRepository;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilderFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/deductions")
public class DeductionController {

    private final DeductionRepository deductionRepository;

    public DeductionController(DeductionRepository deductionRepository) {
        this.deductionRepository = deductionRepository;
    }

    @GetMapping
    public PagedModel<Deduction> getAllDeductions(@RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(defaultValue = "10") int size) {
        List<Deduction> deductions = deductionRepository.findAll(page, size);
        int totalDeductions = deductionRepository.count();
        Pageable pageable = PageRequest.of(page, size);
        PageImpl<Deduction> deductionPage = new PageImpl<>(deductions, pageable, totalDeductions);

        PagedModel.PageMetadata pageMetadata = new PagedModel.PageMetadata(size, page, totalDeductions);
        WebMvcLinkBuilderFactory factory = new WebMvcLinkBuilderFactory();
        WebMvcLinkBuilder linkBuilder = factory.linkTo(WebMvcLinkBuilder.methodOn(DeductionController.class).getAllDeductions(page, size));
        return PagedModel.of(deductions, pageMetadata, linkBuilder.withSelfRel());
    }
}
