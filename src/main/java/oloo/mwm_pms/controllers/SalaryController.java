package oloo.mwm_pms.controllers;

import oloo.mwm_pms.repositories.SalaryRepository;
import oloo.mwm_pms.entinties.Salary;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/salaries")
public class SalaryController {

    private final SalaryRepository salaryRepository;

    public SalaryController(SalaryRepository salaryRepository) {
        this.salaryRepository = salaryRepository;
    }

    @GetMapping("/earnings-deductions/{employeeId}")
    public Map<String, Object> getEarningsAndDeductionsByEmployee(@PathVariable Long employeeId) {
        return salaryRepository.getEarningsAndDeductionsByEmployee(employeeId);
    }

    @GetMapping("/allowances-net-salaries/{departmentId}")
    public List<Map<String, BigDecimal>> getTotalAllowancesAndNetSalariesByDepartment(@PathVariable Long departmentId) {
        return salaryRepository.getTotalAllowancesAndNetSalariesByDepartment(departmentId);
    }

    @GetMapping("/total-net-salary")
    public BigDecimal getTotalNetSalaryToPay() {
        return salaryRepository.getTotalNetSalaryToPay();
    }

    @GetMapping("/payment-history/{employeeId}")
    public List<Map<String, Object>> getPaymentHistoryByEmployee(@PathVariable Long employeeId) {
        return salaryRepository.getPaymentHistoryByEmployee(employeeId);
    }

    @GetMapping
    public List<Salary> getAllSalaries(@RequestParam(defaultValue = "0") int page,
                                       @RequestParam(defaultValue = "10") int size) {
        return salaryRepository.findAll(page, size);
    }

    @GetMapping("/count")
    public int countSalaries() {
        return salaryRepository.count();
    }
}
