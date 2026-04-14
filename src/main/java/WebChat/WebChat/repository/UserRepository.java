package WebChat.WebChat.repository;

import WebChat.WebChat.enity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByUsername(String username);
    List<User> findByFullnameContainingIgnoreCaseOrSdtContaining(String fullname, String sdt);

    boolean existsByUsername(String username);
    boolean existsBySdt(String sdt);
}