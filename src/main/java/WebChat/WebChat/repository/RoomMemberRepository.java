package WebChat.WebChat.repository;

import WebChat.WebChat.enity.RoomMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoomMemberRepository extends JpaRepository<RoomMember, Integer> {

    List<RoomMember> findByRoom_RoomId(String roomId);

    List<RoomMember> findByUser_Username(String username);

    boolean existsByRoom_RoomIdAndUser_Username(String roomId, String username);
}
