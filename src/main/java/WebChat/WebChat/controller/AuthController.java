package WebChat.WebChat.controller;



import WebChat.WebChat.dto.request.LoginRequest;
import WebChat.WebChat.dto.request.RegisterRequest;
import WebChat.WebChat.dto.response.AuthResponse;
import WebChat.WebChat.dto.response.UserResponse;
import WebChat.WebChat.service.AuthService;
import lombok.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest req) {
        return authService.login(req.getUsername(), req.getPassword());
    }

    @PostMapping("/register")
    public Object register(@RequestBody RegisterRequest req) {
        var user = authService.register(
                req.getUsername(),
                req.getPassword(),
                req.getFullname()
        );

        return UserResponse.builder()
                .username(user.getUsername())
                .fullname(user.getFullname())
                .build();
    }
}