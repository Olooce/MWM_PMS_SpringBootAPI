package oloo.mwm_pms.controllers;

import oloo.mwm_pms.entinties.Employee;
import oloo.mwm_pms.services.EmployeeService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }
    @GetMapping
    public List<Employee> getAllEmployee(@RequestParam(defaultValue = "0") int page,
                                       @RequestParam(defaultValue = "10") int size) {
        return employeeService.getAllEmployee(page, size);
    }

    @GetMapping("/new-by-department")
    public List<Employee> getNewEmployeesGroupedByDepartment(@RequestParam LocalDate startDate,
                                                             @RequestParam LocalDate endDate) {
        return employeeService.getNewEmployeesGroupedByDepartment(startDate, endDate);
    }

    @GetMapping("/count-active/{departmentId}")
    public Long countActiveEmployeesInDepartment(@PathVariable Long departmentId) {
        return employeeService.countActiveEmployeesInDepartment(departmentId);
    }

    @PostMapping("/add-employee/{departmentId}")
    public long addNewEmployee(@RequestParam long departmentId){
        Employee employee = new Employee();
        employee.setDepartmentId(departmentId);

        return departmentId;
    }
}
