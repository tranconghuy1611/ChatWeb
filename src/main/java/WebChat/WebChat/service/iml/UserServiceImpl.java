package WebChat.WebChat.service.iml;


import WebChat.WebChat.enity.User;
import WebChat.WebChat.repository.UserRepository;
import WebChat.WebChat.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public List<User> searchUsers(String keyword) {
        return userRepository
                .findByUsernameContainingIgnoreCaseOrSdtContaining(keyword, keyword);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}