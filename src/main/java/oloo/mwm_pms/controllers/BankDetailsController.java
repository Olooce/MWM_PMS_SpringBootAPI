package oloo.mwm_pms.controllers;

import oloo.mwm_pms.entinties.BankDetails;
import oloo.mwm_pms.services.BankDetailsService;
import org.springframework.hateoas.PagedModel;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bankdetails")
public class BankDetailsController {
    private final BankDetailsService bankDetailsService;

    public BankDetailsController(BankDetailsService bankDetailsService) {
        this.bankDetailsService = bankDetailsService;
    }

    @GetMapping
    public PagedModel<BankDetails> getAllBankDetails(@RequestParam(defaultValue = "0") int page,
                                                     @RequestParam(defaultValue = "10") int size) {
        return bankDetailsService.getAllBankDetails(page, size);
    }
}
