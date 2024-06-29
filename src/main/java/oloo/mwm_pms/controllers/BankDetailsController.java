package oloo.mwm_pms.controllers;

import oloo.mwm_pms.entinties.BankDetails;
import oloo.mwm_pms.repositories.BankDetailsRepository;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilderFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bankdetails")
public class BankDetailsController {

    private final BankDetailsRepository bankDetailsRepository;

    public BankDetailsController(BankDetailsRepository bankDetailsRepository) {
        this.bankDetailsRepository = bankDetailsRepository;
    }

    @GetMapping
    public PagedModel<BankDetails> getAllBankDetails(@RequestParam(defaultValue = "0") int page,
                                                     @RequestParam(defaultValue = "10") int size) {
        List<BankDetails> bankDetailsList = bankDetailsRepository.findAll(page, size);
        int totalBankDetails = bankDetailsRepository.count();
        Pageable pageable = PageRequest.of(page, size);
        PageImpl<BankDetails> bankDetailsPage = new PageImpl<>(bankDetailsList, pageable, totalBankDetails);

        PagedModel.PageMetadata pageMetadata = new PagedModel.PageMetadata(size, page, totalBankDetails);
        WebMvcLinkBuilderFactory factory = new WebMvcLinkBuilderFactory();
        WebMvcLinkBuilder linkBuilder = factory.linkTo(WebMvcLinkBuilder.methodOn(BankDetailsController.class).getAllBankDetails(page, size));
        return PagedModel.of(bankDetailsList, pageMetadata, linkBuilder.withSelfRel());
    }

}
