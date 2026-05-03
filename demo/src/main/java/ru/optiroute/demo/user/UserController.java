package ru.optiroute.demo.user;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import ru.optiroute.demo.auth.AuthResponse;
import ru.optiroute.demo.auth.AuthenticatedUser;

@RestController
@RequestMapping("/api/users/me")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public AuthResponse.UserView getProfile(@AuthenticationPrincipal AuthenticatedUser user) {
        return userService.getProfile(user.getUsername());
    }
}
