package WebChat.WebChat.controller;

import WebChat.WebChat.dto.request.UserRequest;
import WebChat.WebChat.dto.response.UserResponse;
import WebChat.WebChat.enity.User;
import WebChat.WebChat.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin("*")
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
                user.getFullname()
        );
    }

    @PostMapping("/login")
    public UserResponse login(@RequestBody UserRequest req) {

        User user = userService.login(
                req.getUsername(),
                req.getPassword()
        );

        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getFullname()
        );
    }

    @GetMapping("/search")
    public List<UserResponse> search(@RequestParam String keyword) {

        List<User> users = userService.searchByFullnameOrSdt(keyword);

        return users.stream().map(user -> new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getFullname()
        )).toList();
    }
    @GetMapping
    public List<User> getAll() {
        return userService.getAllUsers();
    }
}