package oloo.mwm_pms.services;

import oloo.mwm_pms.controllers.DeductionController;
import oloo.mwm_pms.entinties.Deduction;
import oloo.mwm_pms.repositories.DeductionRepository;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilderFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeductionService {
    private final DeductionRepository deductionRepository;

    public DeductionService(DeductionRepository deductionRepository) {
        this.deductionRepository = deductionRepository;
    }

    public PagedModel<Deduction> getAllDeductions(int page, int size) {
        // Fetch one extra record to determine if there are more pages
        List<Deduction> deductions = deductionRepository.findAll(page, size + 1);

        if (deductions == null || deductions.isEmpty()) {
            return null;
        }

        // Check if there is a next page
        boolean hasNext = deductions.size() > size;
        if (hasNext) {
            deductions = deductions.subList(0, size);
        }

        PagedModel.PageMetadata pageMetadata = new PagedModel.PageMetadata(size, page, deductions.size());
        WebMvcLinkBuilderFactory factory = new WebMvcLinkBuilderFactory();
        WebMvcLinkBuilder linkBuilder = factory.linkTo(WebMvcLinkBuilder.methodOn(DeductionController.class).getAllDeductions(page, size));
        PagedModel<Deduction> pagedModel = PagedModel.of(deductions, pageMetadata, linkBuilder.withSelfRel());

        // Add links for next and previous pages, if applicable
        if (page > 0) {
            pagedModel.add(linkBuilder.slash("?page=" + (page - 1) + "&size=" + size).withRel("prev"));
        }
        if (hasNext) {
            pagedModel.add(linkBuilder.slash("?page=" + (page + 1) + "&size=" + size).withRel("next"));
        }

        return pagedModel;
    }
}
