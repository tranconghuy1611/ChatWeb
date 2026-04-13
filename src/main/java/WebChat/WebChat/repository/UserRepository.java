package WebChat.WebChat.repository;

import WebChat.WebChat.enity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    // search user (dùng cho sidebar)
    List<User> findByUsernameContainingIgnoreCase(String keyword);

    List<User> findByUsernameContainingIgnoreCaseOrSdtContaining(String username, String sdt);
}