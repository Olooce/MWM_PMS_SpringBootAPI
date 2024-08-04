package oloo.mwm_pms.services;


import oloo.mwm_pms.controllers.SystemUserController;
import oloo.mwm_pms.entinties.SystemUser;
import oloo.mwm_pms.repositories.SystemUserRepository;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SystemUserService implements UserDetailsService {

    private final SystemUserRepository systemUserRepository;

    public SystemUserService(SystemUserRepository systemUserRepository) {
        this.systemUserRepository = systemUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SystemUser systemUser = systemUserRepository.findByUsername(username);
        if (systemUser == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return new org.springframework.security.core.userdetails.User(
                systemUser.getUsername(),
                systemUser.getPassword(),
                systemUser.getRoles() // Make sure SystemUser has a getRoles method returning Collection<GrantedAuthority>
        );
    }

    public PagedModel<SystemUser> getAllSystemUsers(int page, int size) {
        List<SystemUser> systemUsers = systemUserRepository.findAll(page, size + 1);

        if (systemUsers == null || systemUsers.isEmpty()) {
            return null;
        }

        // Check if there is a next page
        boolean hasNext = systemUsers.size() > size;
        if (hasNext) {
            systemUsers = systemUsers.subList(0, size);
        }

        PagedModel.PageMetadata pageMetadata = new PagedModel.PageMetadata(size, page, systemUsers.size());
        WebMvcLinkBuilder linkBuilder = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(SystemUserController.class).getAllSystemUsers(page, size));
        PagedModel<SystemUser> pagedModel = PagedModel.of(systemUsers, pageMetadata, linkBuilder.withSelfRel());

        // Add links for next and previous pages, if applicable
        if (page > 0) {
            pagedModel.add(linkBuilder.slash("?page=" + (page - 1) + "&size=" + size).withRel("prev"));
        }
        if (hasNext) {
            pagedModel.add(linkBuilder.slash("?page=" + (page + 1) + "&size=" + size).withRel("next"));
        }

        return pagedModel;
    }

    public SystemUser findByUsername(String username) {
        return systemUserRepository.findByUsername(username);
    }

    public boolean checkPassword(String rawPassword, String hashedPassword) {
        String hashedRawPassword = DigestUtils.sha256Hex(rawPassword);
        return hashedRawPassword.equals(hashedPassword);
    }
}
