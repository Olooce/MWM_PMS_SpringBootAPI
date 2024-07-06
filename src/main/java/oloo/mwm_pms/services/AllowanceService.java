package oloo.mwm_pms.services;

import oloo.mwm_pms.controllers.AllowanceController;
import oloo.mwm_pms.entinties.Allowance;
import oloo.mwm_pms.repositories.AllowanceRepository;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AllowanceService {
    private final AllowanceRepository allowanceRepository;

    public AllowanceService(AllowanceRepository allowanceRepository) {
        this.allowanceRepository = allowanceRepository;
    }

    public PagedModel<Allowance> getAllAllowances(int page, int size) {
        // Fetch one extra record to determine if there are more pages
        List<Allowance> allowances = allowanceRepository.findAll(page, size + 1);

        if (allowances == null || allowances.isEmpty()) {
            return null;
        }

        // Check if there is a next page
        boolean hasNext = allowances.size() > size;
        if (hasNext) {
            allowances = allowances.subList(0, size);
        }

        PagedModel.PageMetadata pageMetadata = new PagedModel.PageMetadata(size, page, allowances.size());
        WebMvcLinkBuilder linkBuilder = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(AllowanceController.class).getAllAllowances(page, size));
        PagedModel<Allowance> pagedModel = PagedModel.of(allowances, pageMetadata, linkBuilder.withSelfRel());

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
