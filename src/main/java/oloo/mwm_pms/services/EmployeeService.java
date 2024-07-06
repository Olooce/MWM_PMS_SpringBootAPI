package oloo.mwm_pms.services;

import oloo.mwm_pms.controllers.EmployeeController;
import oloo.mwm_pms.dtos.EmployeeDTO;
import oloo.mwm_pms.entinties.Employee;
import oloo.mwm_pms.entinties.EmploymentType;
import oloo.mwm_pms.entinties.EmployeeStatus;
import oloo.mwm_pms.repositories.EmployeeRepository;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public PagedModel<Employee> getAllEmployee(int page, int size) {
        // Fetch employees
        List<Employee> employees = employeeRepository.findAll(page, size);

        if (employees == null || employees.isEmpty()) {
            return null;
        }

        // Check if there are more employees than the page size to determine if there is a next page
        boolean hasNext = employees.size() > size;
        if (hasNext) {
            employees = employees.subList(0, size);
        }

        PagedModel.PageMetadata pageMetadata = new PagedModel.PageMetadata(size, page, employees.size());
        WebMvcLinkBuilder linkBuilder = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EmployeeController.class).getAllEmployee(page, size));
        PagedModel<Employee> pagedModel = PagedModel.of(employees, pageMetadata, linkBuilder.withSelfRel());

        // Add links for next and previous pages, if applicable
        if (page > 0) {
            pagedModel.add(linkBuilder.slash("?page=" + (page - 1) + "&size=" + size).withRel("prev"));
        }
        if (hasNext) {
            pagedModel.add(linkBuilder.slash("?page=" + (page + 1) + "&size=" + size).withRel("next"));
        }

        return pagedModel;
    }

    public PagedModel<Employee> getNewEmployeesGroupedByDepartment(LocalDate startDate, LocalDate endDate, int page, int size) {
        // Fetch new employees grouped by department
        List<Employee> employees = employeeRepository.findNewEmployeesGroupedByDepartment(startDate, endDate, page, size);

        if (employees == null || employees.isEmpty()) {
            return null;
        }

        // Check if there are more employees than the page size to determine if there is a next page
        boolean hasNext = employees.size() > size;
        if (hasNext) {
            employees = employees.subList(0, size);
        }

        PagedModel.PageMetadata pageMetadata = new PagedModel.PageMetadata(size, page, employees.size());
        WebMvcLinkBuilder linkBuilder = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EmployeeController.class).getNewEmployeesGroupedByDepartment(startDate, endDate, page, size));
        PagedModel<Employee> pagedModel = PagedModel.of(employees, pageMetadata, linkBuilder.withSelfRel());

        // Add links for next and previous pages, if applicable
        if (page > 0) {
            pagedModel.add(linkBuilder.slash("?page=" + (page - 1) + "&size=" + size).withRel("prev"));
        }
        if (hasNext) {
            pagedModel.add(linkBuilder.slash("?page=" + (page + 1) + "&size=" + size).withRel("next"));
        }

        return pagedModel;
    }

    public Long countActiveEmployeesInDepartment(Long departmentId) {
        return employeeRepository.countActiveEmployeesInDepartment(departmentId);
    }

    public Long countNewEmployeesInDepartment(long departmentId) {
        return employeeRepository.countNewEmployeesInDepartment(departmentId);
    }

    public Employee addNewEmployee(EmployeeDTO employeeDto) {
        Employee employee = new Employee();

        employee.setName(employeeDto.getName());
        employee.setDob(employeeDto.getDob());
        employee.setGender(employeeDto.getGender());
        employee.setDepartmentId(employeeDto.getDepartmentId());
        employee.setEmploymentType(EmploymentType.valueOf(employeeDto.getEmploymentType()));
        employee.setEmploymentDate(employeeDto.getEmploymentDate());
        employee.setStatus(EmployeeStatus.valueOf(employeeDto.getStatus()));
        employee.setStatusDescription(employeeDto.getStatusDescription());
        employee.setTerminationDate(employeeDto.getTerminationDate());

        return employeeRepository.addEmployee(employee);
    }
}
