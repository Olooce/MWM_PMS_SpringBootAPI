package oloo.mwm_pms.controllers;


import oloo.mwm_pms.entinties.Allowance;

import oloo.mwm_pms.services.AllowanceService;

import org.springframework.data.domain.PageImpl;

import org.springframework.data.domain.Pageable;

import org.springframework.hateoas.PagedModel;

import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;

import org.springframework.web.bind.annotation.*;


@RestController

@RequestMapping("/allowances")

public class AllowanceController {


    private final AllowanceService allowanceService;


    public AllowanceController(AllowanceService allowanceService) {

        this.allowanceService = allowanceService;

    }


    @GetMapping

    public PagedModel<Allowance> getAllAllowances(@RequestParam(defaultValue = "0") int page,

                                                  @RequestParam(defaultValue = "10") int size) {

        return allowanceService.getAllAllowances(page, size);

    }

}