package oloo.mwm_pms.controllers;

import oloo.mwm_pms.entinties.Allowance;
import oloo.mwm_pms.repositories.AllowanceRepository;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilderFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/allowances")
public class AllowanceController {

    private final AllowanceRepository allowanceRepository;

    public AllowanceController(AllowanceRepository allowanceRepository) {
        this.allowanceRepository = allowanceRepository;
    }

    @GetMapping
    public PagedModel<Allowance> getAllAllowances(@RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(defaultValue = "10") int size) {
        List<Allowance> allowances = allowanceRepository.findAll(page, size);
        int totalAllowances = allowanceRepository.count();
        Pageable pageable = PageRequest.of(page, size);
        PageImpl<Allowance> allowancePage = new PageImpl<>(allowances, pageable, totalAllowances);

        PagedModel.PageMetadata pageMetadata = new PagedModel.PageMetadata(size, page, totalAllowances);
        WebMvcLinkBuilderFactory factory = new WebMvcLinkBuilderFactory();
        WebMvcLinkBuilder linkBuilder = factory.linkTo(WebMvcLinkBuilder.methodOn(AllowanceController.class).getAllAllowances(page, size));
        return PagedModel.of(allowances, pageMetadata, linkBuilder.withSelfRel());
    }
}
