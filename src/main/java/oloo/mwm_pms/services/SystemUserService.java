package oloo.mwm_pms.services;

import oloo.mwm_pms.controllers.SystemUserController;
import oloo.mwm_pms.entinties.SystemUser;
import oloo.mwm_pms.repositories.SystemUserRepository;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilderFactory;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class SystemUserService {
    private final SystemUserRepository systemUserRepository;

    public SystemUserService(SystemUserRepository systemUserRepository) {
        this.systemUserRepository = systemUserRepository;
    }


    public PagedModel<SystemUser> getAllSystemUsers(int page,
                                                     int size) {
        List<SystemUser> systemUsers = systemUserRepository.findAll(page, size);
        int totalSystemUsers = systemUserRepository.count();
        Pageable pageable = PageRequest.of(page, size);
        PageImpl<SystemUser> systemUserPage = new PageImpl<>(systemUsers, pageable, totalSystemUsers);

        PagedModel.PageMetadata pageMetadata = new PagedModel.PageMetadata(size, page, totalSystemUsers);
        WebMvcLinkBuilderFactory factory = new WebMvcLinkBuilderFactory();
        WebMvcLinkBuilder linkBuilder = factory.linkTo(WebMvcLinkBuilder.methodOn(SystemUserController.class).getAllSystemUsers(page, size));
        return PagedModel.of(systemUsers, pageMetadata, linkBuilder.withSelfRel());
    }


    public SystemUser findByUsername(String username) {
        return systemUserRepository.findByUsername(username);
    }

    public boolean checkPassword(String rawPassword, String hashedPassword) {
//        String hashedRawPassword = DigestUtils.sha256Hex(rawPassword);
        String hashedRawpassword = rawPassword;
        return hashedRawPassword.equals(hashedPassword);
    }
}
