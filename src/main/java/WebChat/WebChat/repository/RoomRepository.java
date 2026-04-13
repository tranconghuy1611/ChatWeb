package WebChat.WebChat.repository;

import WebChat.WebChat.enity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoomRepository extends JpaRepository<Room, String> {
    List<Room> findByCreatedBy_Username(String username);
}
