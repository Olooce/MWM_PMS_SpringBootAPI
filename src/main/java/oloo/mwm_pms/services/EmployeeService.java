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
import java.util.Optional;

@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public List<Employee> getAllEmployee(int page, int size) {

        List<Employee> employees = employeeRepository.findAll(page, size);

        if (employees == null || employees.isEmpty()) {
            return null;
        }
        
        return employees;
    }
    public List<Employee> searchEmployees(String searchTerm, int page, int size) {
        return employeeRepository.searchEmployees(searchTerm, page, size);
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

    public Employee updateEmployee(Long employeeId, EmployeeDTO employeeDto) {
        Optional<Employee> employeeOptional = employeeRepository.findById(employeeId);
        if (employeeOptional.isPresent()) {
            Employee employee = employeeOptional.get();
            employee.setName(employeeDto.getName());
            employee.setDob(employeeDto.getDob());
            employee.setGender(employeeDto.getGender());
            employee.setDepartmentId(employeeDto.getDepartmentId());
            employee.setEmploymentType(EmploymentType.valueOf(employeeDto.getEmploymentType()));
            employee.setEmploymentDate(employeeDto.getEmploymentDate());
            employee.setStatus(EmployeeStatus.valueOf(employeeDto.getStatus()));
            employee.setStatusDescription(employeeDto.getStatusDescription());
            employee.setTerminationDate(employeeDto.getTerminationDate());
            return employeeRepository.save(employee);
        } else {
            return null;
        }
    }

    public void deleteEmployee(Long employeeId) {
        employeeRepository.deleteById(employeeId);
    }
}
