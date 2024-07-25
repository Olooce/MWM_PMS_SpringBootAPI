package oloo.mwm_pms.controllers;

import oloo.mwm_pms.entinties.SystemUser;
import oloo.mwm_pms.dtos.AuthRequest;
import oloo.mwm_pms.services.SystemUserService;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

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
    @PostMapping
    public ResponseEntity<String> authenticateUser(
            @RequestBody AuthRequest authRequest,
            HttpServletResponse response) {

        String username = authRequest.getUsername();
        String password = authRequest.getPassword();

        SystemUser user = systemUserService.findByUsername(username);

        if (user == null || !systemUserService.checkPassword(password, user.getPassword())) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        // Generate a session token (or use JWT)
        String sessionToken = generateSessionToken(user);

        // Create a cookie with the session token
        Cookie sessionCookie = new Cookie("SESSIONID", sessionToken);
        sessionCookie.setHttpOnly(true);
        sessionCookie.setSecure(true); // Set to true in production
        sessionCookie.setPath("/");
        sessionCookie.setMaxAge(7 * 24 * 60 * 60); // 7 days

        // Add the cookie to the response
        response.addCookie(sessionCookie);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    private String generateSessionToken(SystemUser user) {
        return "generated_session_token";
    }


}
