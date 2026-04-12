package WebChat.WebChat.repository;

import WebChat.WebChat.enity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, String> {
}
