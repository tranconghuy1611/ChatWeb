package WebChat.WebChat.service.iml;

import WebChat.WebChat.enity.Message;
import WebChat.WebChat.enity.Room;
import WebChat.WebChat.enity.User;
import WebChat.WebChat.repository.MessageRepository;
import WebChat.WebChat.repository.RoomMemberRepository;
import WebChat.WebChat.repository.RoomRepository;
import WebChat.WebChat.repository.UserRepository;
import WebChat.WebChat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    private final RoomMemberRepository roomMemberRepository;

    // =========================
    // PRIVATE CHAT
    // =========================
    @Override
    public Message sendPrivateMessage(String senderUsername, String receiverUsername, String content) {
        if (content == null || content.isBlank()) {
            throw new RuntimeException("Message content must not be empty");
        }

        User sender = userRepository.findByUsername(senderUsername)
                .orElseThrow(() -> new RuntimeException("Sender not found"));

        User receiver = userRepository.findByUsername(receiverUsername)
                .orElseThrow(() -> new RuntimeException("Receiver not found"));

        Message message = Message.builder()
                .sender(sender)
                .receiver(receiver)
                .content(content)
                .messageType("PRIVATE")
                .createdAt(LocalDateTime.now())
                .build();

        return messageRepository.save(message);
    }

    // =========================
    // GROUP CHAT
    // =========================
    @Override
    public Message sendGroupMessage(String senderUsername, String roomId, String content) {
        if (content == null || content.isBlank()) {
            throw new RuntimeException("Message content must not be empty");
        }

        User sender = userRepository.findByUsername(senderUsername)
                .orElseThrow(() -> new RuntimeException("Sender not found"));

        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found"));

        if (!roomMemberRepository.existsByRoomAndUser(room, sender)) {
            throw new RuntimeException("Sender is not a member of this room");
        }

        Message message = Message.builder()
                .sender(sender)
                .room(room)
                .content(content)
                .messageType("GROUP")
                .createdAt(LocalDateTime.now())
                .build();

        return messageRepository.save(message);
    }

    // =========================
    // GET PRIVATE CHAT
    // =========================
    @Override
    public List<Message> getPrivateMessages(String user1, String user2) {

        User u1 = userRepository.findByUsername(user1)
                .orElseThrow(() -> new RuntimeException("User1 not found"));

        User u2 = userRepository.findByUsername(user2)
                .orElseThrow(() -> new RuntimeException("User2 not found"));

        return messageRepository
                .findBySenderAndReceiverOrSenderAndReceiverOrderByCreatedAtAsc(
                        u1, u2,
                        u2, u1
                );
    }

    // =========================
    // GET GROUP CHAT
    // =========================
    @Override
    public List<Message> getRoomMessages(String roomId) {

        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found"));

        return messageRepository.findByRoomOrderByCreatedAtAsc(room);
    }
}