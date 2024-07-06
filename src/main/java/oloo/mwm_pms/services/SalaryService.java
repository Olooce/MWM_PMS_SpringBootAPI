package oloo.mwm_pms.services;

import oloo.mwm_pms.controllers.SalaryController;
import oloo.mwm_pms.entinties.Salary;
import oloo.mwm_pms.repositories.SalaryRepository;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
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

    public PagedModel<Salary> getAllSalaries(int page, int size) {
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

        PagedModel.PageMetadata pageMetadata = new PagedModel.PageMetadata(size, page, salaries.size());
        WebMvcLinkBuilder linkBuilder = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(SalaryController.class).getAllSalaries(page, size));
        PagedModel<Salary> pagedModel = PagedModel.of(salaries, pageMetadata, linkBuilder.withSelfRel());

        // Add links for next and previous pages, if applicable
        if (page > 0) {
            pagedModel.add(linkBuilder.slash("?page=" + (page - 1) + "&size=" + size).withRel("prev"));
        }
        if (hasNext) {
            pagedModel.add(linkBuilder.slash("?page=" + (page + 1) + "&size=" + size).withRel("next"));
        }

        return pagedModel;
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
