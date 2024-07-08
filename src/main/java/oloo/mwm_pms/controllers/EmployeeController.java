package oloo.mwm_pms.controllers;

import oloo.mwm_pms.dtos.EmployeeDTO;
import oloo.mwm_pms.entinties.Employee;
import oloo.mwm_pms.services.EmployeeService;
import org.springframework.hateoas.PagedModel;
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
    public List<Employee> getAllEmployee(@RequestParam(defaultValue = "1") int page,
                                         @RequestParam(defaultValue = "10") int size) {
        return employeeService.getAllEmployee(page, size);
    }

    @GetMapping("/new-by-department")
    public PagedModel<Employee> getNewEmployeesGroupedByDepartment(
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size){

        if (startDate == null) {
            startDate = LocalDate.now().minusMonths(1).withDayOfMonth(1);
        }
        if (endDate == null) {
            //endDate = LocalDate.now().minusDays(1);
            endDate = LocalDate.now();
        }

        return employeeService.getNewEmployeesGroupedByDepartment(startDate, endDate, page, size);
    }

    @GetMapping("/count-active/{departmentId}")
    public Long countActiveEmployeesInDepartment(@PathVariable Long departmentId) {
        return employeeService.countActiveEmployeesInDepartment(departmentId);
    }

    @PostMapping("/add-employee")
    public Employee addNewEmployee(@RequestBody EmployeeDTO employeeDto) {
        return employeeService.addNewEmployee(employeeDto);
    }
}
