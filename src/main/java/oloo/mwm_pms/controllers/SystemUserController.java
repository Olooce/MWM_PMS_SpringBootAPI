package oloo.mwm_pms.controllers;

import oloo.mwm_pms.dtos.AuthRequest;
import oloo.mwm_pms.entinties.SystemUser;
import oloo.mwm_pms.services.SystemUserService;
import oloo.mwm_pms.utils.JwtUtil;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/systemusers")
public class SystemUserController {

    private final SystemUserService systemUserService;
    private final JwtUtil jwtUtil;

    public SystemUserController(SystemUserService systemUserService, JwtUtil jwtUtil) {
        this.systemUserService = systemUserService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping
    public PagedModel<SystemUser> getAllSystemUsers(@RequestParam(defaultValue = "0") int page,
                                                    @RequestParam(defaultValue = "10") int size) {
        return systemUserService.getAllSystemUsers(page, size);
    }

    @PostMapping("/auth")
    public ResponseEntity<String> authenticateUser(@RequestBody AuthRequest authRequest) {
        String username = authRequest.getUsername();
        String password = authRequest.getPassword();

        SystemUser user = systemUserService.findByUsername(username);

        if (user == null || !systemUserService.checkPassword(password, user.getPassword())) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        String jwt = jwtUtil.generateToken(user.getUsername());
        return ResponseEntity.ok(jwt);
    }
}
