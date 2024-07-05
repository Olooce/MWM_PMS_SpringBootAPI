package oloo.mwm_pms.services;

import oloo.mwm_pms.controllers.EmployeeController;
import oloo.mwm_pms.dtos.EmployeeDTO;
import oloo.mwm_pms.entinties.Employee;
import oloo.mwm_pms.entinties.EmploymentType;
import oloo.mwm_pms.entinties.EmployeeStatus;
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

        PagedModel.PageMetadata pageMetadata = new PagedModel.PageMetadata(size,page, totalEmployees);
        WebMvcLinkBuilder linkBuilder = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EmployeeController.class).getAllEmployee(page,size));
        return PagedModel.of(employees, pageMetadata, linkBuilder.withSelfRel());
    }


    public PagedModel<Employee> getNewEmployeesGroupedByDepartment( LocalDate startDate,
                                                              LocalDate endDate, int page, int size) {
        List<Employee> employees = employeeRepository.findNewEmployeesGroupedByDepartment(startDate, endDate, page, size);

        PagedModel.PageMetadata pageMetadata = new PagedModel.PageMetadata(size, page, employees.size());
        WebMvcLinkBuilder linkBuilder = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EmployeeController.class).getNewEmployeesGroupedByDepartment(startDate,endDate, page, size));
        return PagedModel.of(employees, pageMetadata, linkBuilder.withSelfRel());
    }


    public Long countActiveEmployeesInDepartment( Long departmentId) {
        return employeeRepository.countActiveEmployeesInDepartment(departmentId);
    }

    public  Long countNewEmployeesInDepartment(long departmentId){
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
