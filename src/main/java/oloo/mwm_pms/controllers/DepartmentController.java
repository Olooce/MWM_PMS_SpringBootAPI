package oloo.mwm_pms.controllers;


import oloo.mwm_pms.entinties.Department;
import oloo.mwm_pms.services.DepartmentService;
import org.springframework.hateoas.PagedModel;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/departments")
public class DepartmentController {
    private final DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @GetMapping
    public PagedModel<Department> getAllDepartments(@RequestParam(defaultValue = "0") int page,
                                                 @RequestParam(defaultValue = "10") int size) {
        return departmentService.getAllDepartments(page, size);
    }


}
