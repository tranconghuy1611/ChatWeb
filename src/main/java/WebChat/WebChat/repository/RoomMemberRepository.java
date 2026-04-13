package WebChat.WebChat.repository;

import WebChat.WebChat.enity.Room;
import WebChat.WebChat.enity.RoomMember;
import WebChat.WebChat.enity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomMemberRepository extends JpaRepository<RoomMember, Integer> {
    List<RoomMember> findByUser(User user);

    List<RoomMember> findByRoom(Room room);

    boolean existsByRoomAndUser(Room room, User user);
}
