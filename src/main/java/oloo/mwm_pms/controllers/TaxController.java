package oloo.mwm_pms.controllers;

import oloo.mwm_pms.entinties.Tax;
import oloo.mwm_pms.repositories.TaxRepository;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilderFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/taxes")
public class TaxController {

    private final TaxRepository taxRepository;

    public TaxController(TaxRepository taxRepository) {
        this.taxRepository = taxRepository;
    }

    @GetMapping
    public PagedModel<Tax> getAllTaxes(@RequestParam(defaultValue = "0") int page,
                                       @RequestParam(defaultValue = "10") int size) {
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
