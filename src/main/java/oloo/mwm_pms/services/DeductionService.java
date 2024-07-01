package oloo.mwm_pms.services;

import oloo.mwm_pms.controllers.DeductionController;
import oloo.mwm_pms.entinties.Deduction;
import oloo.mwm_pms.repositories.DeductionRepository;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilderFactory;


import java.util.List;

public class DeductionService {
    private final DeductionRepository deductionRepository;

    public DeductionService(DeductionRepository deductionRepository) {
        this.deductionRepository = deductionRepository;
    }

    public PagedModel<Deduction> getAllDeductions(int page, int size) {
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
