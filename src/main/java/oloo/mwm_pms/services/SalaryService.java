package oloo.mwm_pms.services;

import oloo.mwm_pms.entinties.Salary;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
@Service
public class SalaryService {
    private final SalaryService salaryService;

    public SalaryController(SalaryService salaryService {
        this.salaryService = salaryService;
    }

    @GetMapping("/earnings-deductions/{employeeId}")
    public Map<String, Object> getEarningsAndDeductionsByEmployee(@PathVariable Long employeeId) {
        return salaryRepository.getEarningsAndDeductionsByEmployee(employeeId);
    }


    public List<Map<String, BigDecimal>> getTotalAllowancesAndNetSalariesByDepartment(@PathVariable Long departmentId) {
        return salaryRepository.getTotalAllowancesAndNetSalariesByDepartment(departmentId);
    }


    public BigDecimal getTotalNetSalaryToPay() {
        return salaryRepository.getTotalNetSalaryToPay();
    }


    public List<Map<String, Object>> getPaymentHistoryByEmployee(@PathVariable Long employeeId) {
        return salaryRepository.getPaymentHistoryByEmployee(employeeId);
    }


    public List<Salary> getAllSalaries(@RequestParam(defaultValue = "0") int page,
                                       @RequestParam(defaultValue = "10") int size) {
        return salaryRepository.findAll(page, size);
    }

    public int countSalaries() {
        return salaryRepository.count();
    }
}
