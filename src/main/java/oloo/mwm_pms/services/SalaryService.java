package oloo.mwm_pms.services;

import oloo.mwm_pms.entinties.Salary;
import oloo.mwm_pms.repositories.SalaryRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
public class SalaryService {
    private final SalaryRepository salaryRepository;

    public SalaryService(SalaryRepository salaryRepository) {
        this.salaryRepository = salaryRepository;
    }

    public List<Salary> getAllSalaries(int page, int size) {
        // Fetch one extra record to determine if there are more pages
        List<Salary> salaries = salaryRepository.findAll(page, size + 1);

        if (salaries == null || salaries.isEmpty()) {
            return null;
        }

        // Check if there is a next page
        boolean hasNext = salaries.size() > size;
        if (hasNext) {
            salaries = salaries.subList(0, size);
        }


        return salaries;
    }

    public Map<String, Object> getEarningsAndDeductionsByEmployee(Long employeeId) {
        return salaryRepository.getEarningsAndDeductionsByEmployee(employeeId);
    }

    public List<Map<String, BigDecimal>> getTotalAllowancesAndNetSalariesByDepartment(Long departmentId) {
        return salaryRepository.getTotalAllowancesAndNetSalariesByDepartment(departmentId);
    }

    public BigDecimal getTotalNetSalaryToPay() {
        return salaryRepository.getTotalNetSalaryToPay();
    }

    public List<Map<String, Object>> getPaymentHistoryByEmployee(Long employeeId) {
        return salaryRepository.getPaymentHistoryByEmployee(employeeId);
    }

    public int countSalaries() {
        return salaryRepository.count();
    }
}
