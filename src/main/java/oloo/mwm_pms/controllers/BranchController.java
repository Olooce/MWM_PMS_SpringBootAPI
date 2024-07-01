package oloo.mwm_pms.controllers;

import oloo.mwm_pms.services.BranchService;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/branches")
public class BranchController {
    private final BranchService branchService;

   public BranchController(BranchService branchService) {
       this.branchService = branchService;
   }

   @GetMapping public List<Branch> getAllBranches(@RequestParam(defaultValue = 0) int page,
                                                  @RequestParam(defaultValue = 10) int size) {
       return branchService.getAllBranches(page, size);
   }

}
