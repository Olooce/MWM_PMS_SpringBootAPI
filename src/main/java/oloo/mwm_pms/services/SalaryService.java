package oloo.mwm_pms.services;

import oloo.mwm_pms.controllers.AllowanceController;
import oloo.mwm_pms.controllers.SalaryController;
import oloo.mwm_pms.entinties.Salary;
import oloo.mwm_pms.repositories.SalaryRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;
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


    public PagedModel<Salary> getAllSalaries(int page,
                                             int size) {
        List<Salary>  salaries = salaryRepository.findAll(page, size);
        int salaryCount = salaryRepository.count();
        Pageable pageable = PageRequest.of(page, size);

        PagedModel.PageMetadata pageMetadata = new PagedModel.PageMetadata(size, page, salaryCount);
        WebMvcLinkBuilder linkBuilder = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(SalaryController.class).getAllSalaries(page, size));
        return PagedModel.of(salaries, pageMetadata, linkBuilder.withSelfRel());
    }

    public int countSalaries() {
        return salaryRepository.count();
    }
}
