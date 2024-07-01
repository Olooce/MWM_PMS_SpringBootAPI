package oloo.mwm_pms.services;


import oloo.mwm_pms.entinties.Allowance;

import oloo.mwm_pms.repositories.AllowanceRepository;

import org.springframework.data.domain.PageImpl;

import org.springframework.data.domain.Pageable;

import org.springframework.hateoas.PagedModel;

import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;


public class AllowanceService {


    private final AllowanceRepository allowanceRepository;


    public AllowanceService(AllowanceRepository allowanceRepository) {

        this.allowanceRepository = allowanceRepository;

    }


    public PagedModel<Allowance> getAllAllowances(int page, int size) {

        List<Allowance> allowances = allowanceRepository.findAll(page, size);

        int totalAllowances = allowanceRepository.count();

        Pageable pageable = PageRequest.of(page, size);

        PageImpl<Allowance> allowancePage = new PageImpl<>(allowances, pageable, totalAllowances);


        PagedModel.PageMetadata pageMetadata = new PagedModel.PageMetadata(size, page, totalAllowances);

        WebMvcLinkBuilder linkBuilder = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(AllowanceController.class).getAllAllowances(page, size));

        return PagedModel.of(allowances, pageMetadata, linkBuilder.withSelfRel());

    }

}