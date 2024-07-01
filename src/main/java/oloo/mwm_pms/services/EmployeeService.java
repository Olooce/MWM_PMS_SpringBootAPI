package oloo.mwm_pms.services;

import oloo.mwm_pms.controllers.EmployeeController;
import oloo.mwm_pms.entinties.Employee;
import oloo.mwm_pms.repositories.EmployeeRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    public PagedModel<Employee> getAllEmployee( int page,
                                         int size) {
        List<Employee> employees = employeeRepository.findAll(page, size);
        int totalEmployees = employeeRepository.count();
        Pageable pageable = PageRequest.of(page,size);

        PagedModel.PageMetadata pageMetadata = new PagedModel.PageMetadata(page, size, totalEmployees);
        WebMvcLinkBuilder linkBuilder = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EmployeeController.class).getAllEmployee(page,size));
        return PagedModel.of(employees, pageMetadata, linkBuilder.withSelfRel());
    }


    public List<Employee> getNewEmployeesGroupedByDepartment( LocalDate startDate,
                                                              LocalDate endDate) {
        return employeeRepository.findNewEmployeesGroupedByDepartment(startDate, endDate);
    }


    public Long countActiveEmployeesInDepartment( Long departmentId) {
        return employeeRepository.countActiveEmployeesInDepartment(departmentId);
    }

    public long addNewEmployee(long departmentId){
        Employee employee = new Employee();
        employee.setDepartmentId(departmentId);

        return departmentId;
    }
}
