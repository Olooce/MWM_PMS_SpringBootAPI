package oloo.mwm_pms.controllers;

import oloo.mwm_pms.entinties.Employee;
import oloo.mwm_pms.repositories.EmployeeRepository;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeRepository employeeRepository;

    public EmployeeController(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }
    @GetMapping
    public List<Employee> getAllEmployees() {}

    @GetMapping("/new-by-department")
    public List<Employee> getNewEmployeesGroupedByDepartment(@RequestParam LocalDate startDate,
                                                             @RequestParam LocalDate endDate) {
        return employeeRepository.findNewEmployeesGroupedByDepartment(startDate, endDate);
    }

    @GetMapping("/count-active/{departmentId}")
    public Long countActiveEmployeesInDepartment(@PathVariable Long departmentId) {
        return employeeRepository.countActiveEmployeesInDepartment(departmentId);
    }

    @PostMapping("/add-employee/{departmentId}")
    public long addNewEmployee(@RequestParam long departmentId){
        Employee employee = new Employee();
        employee.setDepartmentId(departmentId);

        return departmentId;
    }
}
