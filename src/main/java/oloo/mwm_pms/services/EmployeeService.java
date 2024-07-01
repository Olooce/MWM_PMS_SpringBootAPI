package oloo.mwm_pms.services;

import oloo.mwm_pms.entinties.Employee;
import oloo.mwm_pms.repositories.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public PageModel<Employee> getAllEmployee( int page,
                                         int size) {
        List<Employee> employees = employeeRepository.findAll(page, size);
        int totalEmployees = employeeRepository.count();
        return employeeRepository.findAll(page, size);
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
