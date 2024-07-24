package oloo.mwm_pms.services;

import oloo.mwm_pms.controllers.TaxController;

import oloo.mwm_pms.entinties.Tax;
import oloo.mwm_pms.repositories.TaxRepository;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaxService {
    private final TaxRepository taxRepository;

    public TaxService(TaxRepository taxRepository) {
        this.taxRepository = taxRepository;
    }


    public PagedModel<Tax> getAllTaxes(int page,
                                       int size) {
        // Fetch one extra record to determine if there are more pages
        List<Tax> taxes = taxRepository.findAll(page, size + 1);

        if (taxes == null || taxes.isEmpty()) {
            return null;
        }

        // Check if there is a next page
        boolean hasNext = taxes.size() > size;
        if (hasNext) {
            taxes = taxes.subList(0, size);
        }

        PagedModel.PageMetadata pageMetadata = new PagedModel.PageMetadata(size, page, taxes.size());
        WebMvcLinkBuilder linkBuilder = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(TaxController.class).getAllTaxes(page, size));
        PagedModel<Tax> pagedModel = PagedModel.of(taxes, pageMetadata, linkBuilder.withSelfRel());

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
