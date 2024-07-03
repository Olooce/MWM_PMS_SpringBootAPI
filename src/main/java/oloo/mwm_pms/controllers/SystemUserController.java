package oloo.mwm_pms.controllers;

import oloo.mwm_pms.entinties.SystemUser;
import oloo.mwm_pms.services.SystemUserService;
import org.springframework.hateoas.PagedModel;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/systemusers")
public class SystemUserController {

    private final SystemUserService systemUserService;

    public SystemUserController(SystemUserService systemUserService) {
        this.systemUserService  = systemUserService;

    }

    @GetMapping
    public PagedModel<SystemUser> getAllSystemUsers(@RequestParam(defaultValue = "0") int page,
                                                    @RequestParam(defaultValue = "10") int size) {
       return systemUserService.getAllSystemUsers(page, size);
    }

    @GetMapping("/auth")
    public

}
