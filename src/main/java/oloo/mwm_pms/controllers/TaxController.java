package oloo.mwm_pms.controllers;

import oloo.mwm_pms.entinties.Tax;
import oloo.mwm_pms.repositories.TaxRepository;
import oloo.mwm_pms.services.TaxService;
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
    private final TaxService taxService;

    public TaxController(TaxService taxService) {

        this.taxService = taxService;
    }

    @GetMapping
    public PagedModel<Tax> getAllTaxes(@RequestParam(defaultValue = "0") int page,
                                       @RequestParam(defaultValue = "10") int size) {
        return taxService.getAllTaxes(page,size);
    }
}
