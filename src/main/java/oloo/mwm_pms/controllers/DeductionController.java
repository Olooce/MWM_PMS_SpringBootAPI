package oloo.mwm_pms.controllers;

import oloo.mwm_pms.entinties.Deduction;
import oloo.mwm_pms.services.DeductionService;
import org.springframework.hateoas.PagedModel;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/deductions")
public class DeductionController {
    private final DeductionService deductionService;

    public DeductionController(DeductionService deductionService) {
        this.deductionService = deductionService;
    }

    @GetMapping
    public PagedModel<Deduction> getAllDeductions(@RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(defaultValue = "10") int size){
        return deductionService.getAllDeductions(page, size);
    }

}
