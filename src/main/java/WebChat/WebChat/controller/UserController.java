package WebChat.WebChat.controller;

import WebChat.WebChat.dto.request.UserRequest;
import WebChat.WebChat.dto.response.AuthResponse;
import WebChat.WebChat.dto.response.UserResponse;
import WebChat.WebChat.enity.User;
import WebChat.WebChat.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = {
        "http://localhost:5173",
        "http://127.0.0.1:5173",
        "http://localhost:3000",
        "http://127.0.0.1:3000"
})
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public UserResponse register(@RequestBody UserRequest req) {

        User user = userService.createUser( req.getUsername(),
                req.getPassword(),
                req.getFullname(),
                req.getSdt());

        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getFullname(),
                user.getRole()
        );
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody UserRequest req) {

        User user = userService.login(
                req.getUsername(),
                req.getPassword()
        );
        String token = userService.generateAccessToken(user);

        return new AuthResponse(
                user.getId(),
                user.getUsername(),
                user.getFullname(),
                user.getRole(),
                token
        );
    }

    @GetMapping("/search")
    public List<UserResponse> search(@RequestParam String keyword) {

        List<User> users = userService.searchByFullnameOrSdt(keyword);

        return users.stream().map(user -> new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getFullname(),
                user.getRole()
        )).toList();
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponse> getAll() {
        return userService.getAllUsers().stream()
                .map(user -> new UserResponse(
                        user.getId(),
                        user.getUsername(),
                        user.getFullname(),
                        user.getRole()
                ))
                .toList();
    }

    @GetMapping("/me")
    public UserResponse me(Principal principal) {
        User user = userService.getByUsername(principal.getName());
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getFullname(),
                user.getRole()
        );
    }
}