package WebChat.WebChat.service;

import WebChat.WebChat.enity.User;
import WebChat.WebChat.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import WebChat.WebChat.repository.UserRepository;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    public User createUser(String username, String password, String fullname, String sdt) {

        if (userRepo.existsByUsername(username)) {
            throw new RuntimeException("Username đã tồn tại!");
        }
        if (userRepo.existsBySdt(sdt)) {
            throw new RuntimeException("sdt này đã tồn tại!");
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setFullname(fullname);
        user.setSdt(sdt);
        user.setRole("USER");

        return userRepo.save(user);
    }

    public User login(String username, String password) {

        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User không tồn tại!"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Sai mật khẩu!");
        }

        return user;
    }

    public String generateAccessToken(User user) {
        return jwtService.generateToken(user.getUsername(), user.getRole());
    }

    public List<User> searchByFullnameOrSdt(String keyword) {
        return userRepo.findByFullnameContainingIgnoreCaseOrSdtContaining(keyword, keyword);
    }

    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    public User getByUsername(String username) {
        return userRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
