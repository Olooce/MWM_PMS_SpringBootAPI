package oloo.mwm_pms.controllers;

import oloo.mwm_pms.entinties.Employee;
import oloo.mwm_pms.repositories.EmployeeRepository;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilderFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeRepository employeeRepository;

    public EmployeeController(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @GetMapping
    public PagedModel<Employee> getAllEmployees(@RequestParam(defaultValue = "0") int page,
                                                @RequestParam(defaultValue = "10") int size) {
        List<Employee> employees = employeeRepository.findAll(page, size);
        int totalEmployees = employeeRepository.count();
        Pageable pageable = PageRequest.of(page, size);
        PageImpl<Employee> employeePage = new PageImpl<>(employees, pageable, totalEmployees);

        PagedModel.PageMetadata pageMetadata = new PagedModel.PageMetadata(size, page, totalEmployees);
        WebMvcLinkBuilderFactory factory = new WebMvcLinkBuilderFactory();
        WebMvcLinkBuilder linkBuilder = factory.linkTo(WebMvcLinkBuilder.methodOn(EmployeeController.class).getAllEmployees(page, size));
        return PagedModel.of(employees, pageMetadata, linkBuilder.withSelfRel());
    }
}
