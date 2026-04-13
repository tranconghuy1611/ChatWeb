package WebChat.WebChat.repository;

import WebChat.WebChat.enity.Message;
import WebChat.WebChat.enity.Room;
import WebChat.WebChat.enity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    // =========================
    // CHAT RIÊNG
    // =========================
    List<Message> findBySenderAndReceiverOrderByCreatedAtAsc(User sender, User receiver);

    List<Message> findByReceiverAndSenderOrderByCreatedAtAsc(User receiver, User sender);

    // Lấy full cuộc hội thoại 2 người
    List<Message> findBySenderAndReceiverOrSenderAndReceiverOrderByCreatedAtAsc(
            User sender1, User receiver1,
            User sender2, User receiver2
    );

    // =========================
    // CHAT NHÓM
    // =========================
    List<Message> findByRoomOrderByCreatedAtAsc(Room room);

}