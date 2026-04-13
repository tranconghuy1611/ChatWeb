package WebChat.WebChat.controller;


import WebChat.WebChat.dto.response.UserResponse;
import WebChat.WebChat.enity.User;
import WebChat.WebChat.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // 🔍 search user
    @GetMapping("/search")
    public List<UserResponse> searchUsers(@RequestParam String keyword) {
        return userService.searchUsers(keyword)
                .stream()
                .map(u -> UserResponse.builder()
                        .username(u.getUsername())
                        .fullname(u.getFullname())
                        .sdt(u.getSdt())
                        .build())
                .toList();
    }
    @GetMapping("/me")
    public UserResponse getMyInfo(Authentication authentication) {

        String username = authentication.getName();

        User user = userService.findByUsername(username);

        return UserResponse.builder()
                .username(user.getUsername())
                .fullname(user.getFullname())
                .sdt(user.getSdt())
                .build();
    }
}