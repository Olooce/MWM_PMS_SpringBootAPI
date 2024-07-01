package oloo.mwm_pms.services;

import oloo.mwm_pms.controllers.DepartmentController;
import oloo.mwm_pms.entinties.Department;
import oloo.mwm_pms.repositories.DepartmentRepository;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilderFactory;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class DepartmentService {
    private final DepartmentRepository departmentRepository;

    public DepartmentService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    public PagedModel<Department> getAllDepartments(int page, int size) {
        List<Department> departments = departmentRepository.findAll(page, size);
        int totalDepartments = departmentRepository.count();
        Pageable pageable = PageRequest.of(page, size);
        PageImpl<Department> departmentPage = new PageImpl<>(departments, pageable, totalDepartments);

        PagedModel.PageMetadata pageMetadata = new PagedModel.PageMetadata(size, page, totalDepartments);
        WebMvcLinkBuilderFactory factory = new WebMvcLinkBuilderFactory();
        WebMvcLinkBuilder linkBuilder = factory.linkTo(WebMvcLinkBuilder.methodOn(DepartmentController.class).getAllDepartments(page, size));
        return PagedModel.of(departments, pageMetadata, linkBuilder.withSelfRel());
    }
}
