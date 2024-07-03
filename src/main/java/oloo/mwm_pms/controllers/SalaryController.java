package oloo.mwm_pms.controllers;

import oloo.mwm_pms.entinties.Salary;
import oloo.mwm_pms.services.SalaryService;
import org.springframework.hateoas.PagedModel;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/salaries")
public class SalaryController {

    private final SalaryService salaryService;

    public SalaryController(SalaryService salaryService) {
        this.salaryService = salaryService;
    }

    @GetMapping("/earnings-deductions/{employeeId}")
    public Map<String, Object> getEarningsAndDeductionsByEmployee(@PathVariable Long employeeId) {
        return salaryService.getEarningsAndDeductionsByEmployee(employeeId);
    }

    @GetMapping("/allowances-net-salaries/{departmentId}")
    public List<Map<String, BigDecimal>> getTotalAllowancesAndNetSalariesByDepartment(@PathVariable Long departmentId) {
        return salaryService.getTotalAllowancesAndNetSalariesByDepartment(departmentId);
    }

    @GetMapping("/total-net-salary")
    public BigDecimal getTotalNetSalaryToPay() {
        return salaryService.getTotalNetSalaryToPay();
    }

    @GetMapping("/payment-history/{employeeId}")
    public List<Map<String, Object>> getPaymentHistoryByEmployee(@PathVariable Long employeeId) {
        return salaryService.getPaymentHistoryByEmployee(employeeId);
    }

    @GetMapping
    public PagedModel<Salary> getAllSalaries(@RequestParam(defaultValue = "0") int page,
                                             @RequestParam(defaultValue = "10") int size) {
        return salaryService.getAllSalaries(page, size);
    }

    @GetMapping("/count")
    public int countSalaries() {
        return salaryService.countSalaries();
    }

}
