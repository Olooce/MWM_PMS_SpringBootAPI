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


    public Map<String, Object> getEarningsAndDeductionsByEmployee( Long employeeId) {
        return salaryRepository.getEarningsAndDeductionsByEmployee(employeeId);
    }


    public List<Map<String, BigDecimal>> getTotalAllowancesAndNetSalariesByDepartment( Long departmentId) {
        return salaryRepository.getTotalAllowancesAndNetSalariesByDepartment(departmentId);
    }


    public BigDecimal getTotalNetSalaryToPay() {
        return salaryRepository.getTotalNetSalaryToPay();
    }


    public List<Map<String, Object>> getPaymentHistoryByEmployee( Long employeeId) {
        return salaryRepository.getPaymentHistoryByEmployee(employeeId);
    }


    public List<Salary> getAllSalaries(int page,
                                       int size) {
        return salaryRepository.findAll(page, size);
    }

    public int countSalaries() {
        return salaryRepository.count();
    }
}
