package WebChat.WebChat.service.iml;



import WebChat.WebChat.dto.response.AuthResponse;
import WebChat.WebChat.enity.User;
import WebChat.WebChat.repository.UserRepository;
import WebChat.WebChat.security.JwtUtil;
import WebChat.WebChat.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    @Override
    public AuthResponse login(String username, String password) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Wrong password");
        }

        String token = jwtUtil.generateToken(username);

        return AuthResponse.builder()
                .token(token)
                .username(username)
                .build();
    }

    @Override
    public User register(String username, String password, String fullname) {

        if (userRepository.existsByUsername(username)) {
            throw new RuntimeException("Username already exists");
        }

        User user = User.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .fullname(fullname)
                .role("USER")
                .build();

        return userRepository.save(user);
    }
}