package oloo.mwm_pms.controllers;

import oloo.mwm_pms.entities.SystemUser;
import oloo.mwm_pms.repositories.SystemUserRepository;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilderFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/systemusers")
public class SystemUserController {

    private final SystemUserRepository systemUserRepository;

    public SystemUserController(SystemUserRepository systemUserRepository) {
        this.systemUserRepository = systemUserRepository;
    }

    @GetMapping
    public PagedModel<SystemUser> getAllSystemUsers(@RequestParam(defaultValue = "0") int page,
                                                    @RequestParam(defaultValue = "10") int size) {
        List<SystemUser> systemUsers = systemUserRepository.findAll(page, size);
        int totalSystemUsers = systemUserRepository.count();
        Pageable pageable = PageRequest.of(page, size);
        PageImpl<SystemUser> systemUserPage = new PageImpl<>(systemUsers, pageable, totalSystemUsers);

        PagedModel.PageMetadata pageMetadata = new PagedModel.PageMetadata(size, page, totalSystemUsers);
        WebMvcLinkBuilderFactory factory = new WebMvcLinkBuilderFactory();
        WebMvcLinkBuilder linkBuilder = factory.linkTo(WebMvcLinkBuilder.methodOn(SystemUserController.class).getAllSystemUsers(page, size));
        return PagedModel.of(systemUsers, pageMetadata, linkBuilder.withSelfRel());
    }
}
