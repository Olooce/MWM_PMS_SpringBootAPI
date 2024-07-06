package oloo.mwm_pms.services;

import oloo.mwm_pms.controllers.DepartmentController;
import oloo.mwm_pms.entinties.Department;
import oloo.mwm_pms.repositories.DepartmentRepository;
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
        // Fetch one extra record to determine if there are more pages
        List<Department> departments = departmentRepository.findAll(page, size + 1);

        if (departments == null || departments.isEmpty()) {
            return null;
        }

        // Check if there is a next page
        boolean hasNext = departments.size() > size;
        if (hasNext) {
            departments = departments.subList(0, size);
        }

        PagedModel.PageMetadata pageMetadata = new PagedModel.PageMetadata(size, page, departments.size());
        WebMvcLinkBuilderFactory factory = new WebMvcLinkBuilderFactory();
        WebMvcLinkBuilder linkBuilder = factory.linkTo(WebMvcLinkBuilder.methodOn(DepartmentController.class).getAllDepartments(page, size));
        PagedModel<Department> pagedModel = PagedModel.of(departments, pageMetadata, linkBuilder.withSelfRel());

        // Add links for next and previous pages, if applicable
        if (page > 0) {
            pagedModel.add(linkBuilder.slash("?page=" + (page - 1) + "&size=" + size).withRel("prev"));
        }
        if (hasNext) {
            pagedModel.add(linkBuilder.slash("?page=" + (page + 1) + "&size=" + size).withRel("next"));
        }

        return pagedModel;
    }
}
