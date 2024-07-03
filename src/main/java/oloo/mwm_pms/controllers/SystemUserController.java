package oloo.mwm_pms.controllers;

import oloo.mwm_pms.entinties.SystemUser;
import oloo.mwm_pms.entinties.pojos.AuthRequest;
import oloo.mwm_pms.services.SystemUserService;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    @PostMapping("/auth")
    public ResponseEntity<String> authenticateUser(@RequestBody AuthRequest authRequest) {
        // Extract username and password from the AuthRequest object
        String username = authRequest.getUsername();
        String password = authRequest.getPassword();

        // Find the user by username
        SystemUser user = systemUserService.findByUsername(username);

        // If user not found or password doesn't match, return unsuccessful authentication status
        if (user == null || !systemUserService.checkPassword(password, user.getPassword())) {
            System.out.println((user == null));
            System.out.println(systemUserService.checkPassword(password, user.getPassword()));
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        // Return successful authentication status
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
