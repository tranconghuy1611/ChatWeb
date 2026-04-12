package WebChat.WebChat.service;

import WebChat.WebChat.enity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import WebChat.WebChat.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepo;

    public User createUser(String username, String password, String fullname, String sdt) {

        if (userRepo.existsByUsername(username)) {
            throw new RuntimeException("Username đã tồn tại!");
        }
        if (userRepo.existsBySdtContaining(sdt)) {
            throw new RuntimeException("sdt này đã tồn tại!");
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setFullname(fullname);
        user.setSdt(sdt);

        return userRepo.save(user);
    }

    public User login(String username, String password) {

        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User không tồn tại!"));

        if (!user.getPassword().equals(password)) {
            throw new RuntimeException("Sai mật khẩu!");
        }

        return user;
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
