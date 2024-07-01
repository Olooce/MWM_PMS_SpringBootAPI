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

}
