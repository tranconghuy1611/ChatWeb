package WebChat.WebChat.repository;

import WebChat.WebChat.enity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    // CHAT RIÊNG
    @Query("""
        SELECT m FROM Message m
        WHERE (m.sender.username = :u1 AND m.receiver.username = :u2)
           OR (m.sender.username = :u2 AND m.receiver.username = :u1)
        ORDER BY m.createdAt ASC
    """)
    List<Message> getPrivateChat(String u1, String u2);

    // CHAT NHÓM
    List<Message> findByRoom_RoomIdOrderByCreatedAtAsc(String roomId);

    // LẤY TIN NHẮN MỚI NHẤT
    Message findTopByRoom_RoomIdOrderByCreatedAtDesc(String roomId);

    // ĐẾM TIN NHẮN
    long countByRoom_RoomId(String roomId);
}