package oloo.mwm_pms.services;

import oloo.mwm_pms.controllers.BankDetailsController;
import oloo.mwm_pms.entinties.BankDetails;
import oloo.mwm_pms.repositories.BankDetailsRepository;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilderFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BankDetailsService {
    private final BankDetailsRepository bankDetailsRepository;

    public BankDetailsService(BankDetailsRepository bankDetailsRepository) {
        this.bankDetailsRepository = bankDetailsRepository;
    }

    public PagedModel<BankDetails> getAllBankDetails(int page, int size) {
        // Fetch one extra record to determine if there are more pages
        List<BankDetails> bankDetailsList = bankDetailsRepository.findAll(page, size + 1);

        if (bankDetailsList == null || bankDetailsList.isEmpty()) {
            return null;
        }

        // Check if there is a next page
        boolean hasNext = bankDetailsList.size() > size;
        if (hasNext) {
            bankDetailsList = bankDetailsList.subList(0, size);
        }

        PagedModel.PageMetadata pageMetadata = new PagedModel.PageMetadata(size, page, bankDetailsList.size());
        WebMvcLinkBuilderFactory factory = new WebMvcLinkBuilderFactory();
        WebMvcLinkBuilder linkBuilder = factory.linkTo(WebMvcLinkBuilder.methodOn(BankDetailsController.class).getAllBankDetails(page, size));
        PagedModel<BankDetails> pagedModel = PagedModel.of(bankDetailsList, pageMetadata, linkBuilder.withSelfRel());

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
