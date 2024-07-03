package oloo.mwm_pms.services;

import oloo.mwm_pms.controllers.TaxController;
import oloo.mwm_pms.entinties.Tax;
import oloo.mwm_pms.repositories.TaxRepository;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilderFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaxService {
    private final TaxRepository taxRepository;

    public TaxService(TaxRepository taxRepository) {
        this.taxRepository = taxRepository;
    }


    public PagedModel<Tax> getAllTaxes( int page,
                                        int size) {
        List<Tax> taxes = taxRepository.findAll(page, size);
        int totalTaxes = taxRepository.count();
        Pageable pageable = PageRequest.of(page, size);
        PageImpl<Tax> taxPage = new PageImpl<>(taxes, pageable, totalTaxes);

        PagedModel.PageMetadata pageMetadata = new PagedModel.PageMetadata(size, page, totalTaxes);
        WebMvcLinkBuilderFactory factory = new WebMvcLinkBuilderFactory();
        WebMvcLinkBuilder linkBuilder = factory.linkTo(WebMvcLinkBuilder.methodOn(TaxController.class).getAllTaxes(page, size));
        return PagedModel.of(taxes, pageMetadata, linkBuilder.withSelfRel());
    }
}
