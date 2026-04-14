package WebChat.WebChat.service;

import WebChat.WebChat.enity.Message;
import WebChat.WebChat.enity.Room;
import WebChat.WebChat.enity.RoomMember;
import WebChat.WebChat.enity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import WebChat.WebChat.repository.MessageRepository;
import WebChat.WebChat.repository.RoomMemberRepository;
import WebChat.WebChat.repository.RoomRepository;
import WebChat.WebChat.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private RoomRepository roomRepo;

    @Autowired
    private RoomMemberRepository roomMemberRepo;

    // chat riêng theo room 1-1
    @Transactional
    public Message savePrivateInRoom(String sender, String receiver, String roomId, String content) {
        if (sender == null || sender.isBlank() || receiver == null || receiver.isBlank()) {
            throw new RuntimeException("Sender/receiver không hợp lệ");
        }
        if (sender.equals(receiver)) {
            throw new RuntimeException("Không thể gửi tin nhắn cho chính mình");
        }

        User s = userRepo.findByUsername(sender)
                .orElseThrow(() -> new RuntimeException("Sender không tồn tại"));
        User r = userRepo.findByUsername(receiver)
                .orElseThrow(() -> new RuntimeException("Receiver không tồn tại"));
        String resolvedRoomId = (roomId == null || roomId.isBlank())
                ? buildPrivateRoomId(sender, receiver)
                : roomId;

        Room room = roomRepo.findById(resolvedRoomId)
                .orElseGet(() -> createPrivateRoom(resolvedRoomId, s, r));

        ensureRoomMember(room, s);
        ensureRoomMember(room, r);

        Message m = new Message();
        m.setSender(s);
        m.setReceiver(r);
        m.setRoom(room);
        m.setContent(content);

        return messageRepo.save(m);
    }

    // chat nhóm
    public Message saveRoom(String sender, String roomId, String content) {
        if (sender == null || sender.isBlank() || roomId == null || roomId.isBlank()) {
            throw new RuntimeException("Sender/room không hợp lệ");
        }
        if (!roomMemberRepo.existsByRoom_RoomIdAndUser_Username(roomId, sender)) {
            throw new RuntimeException("User không thuộc room này");
        }

        User s = userRepo.findByUsername(sender)
                .orElseThrow(() -> new RuntimeException("Sender không tồn tại"));
        Room room = roomRepo.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room không tồn tại"));

        Message m = new Message();
        m.setSender(s);
        m.setReceiver(null);
        m.setRoom(room);
        m.setContent(content);

        return messageRepo.save(m);
    }

    public List<Message> getPrivateChat(String u1, String u2) {
        return messageRepo.getPrivateChat(u1, u2);
    }

    public List<Message> getRoomChat(String roomId) {
        return messageRepo.findByRoom_RoomIdOrderByCreatedAtAsc(roomId);
    }

    private void ensureRoomMember(Room room, User user) {
        if (roomMemberRepo.existsByRoom_RoomIdAndUser_Username(room.getRoomId(), user.getUsername())) {
            return;
        }
        RoomMember member = new RoomMember();
        member.setRoom(room);
        member.setUser(user);
        roomMemberRepo.save(member);
    }

    private Room createPrivateRoom(String roomId, User sender, User receiver) {
        Room room = new Room();
        room.setRoomId(roomId);
        room.setRoomName("Private: " + sender.getUsername() + " - " + receiver.getUsername());
        return roomRepo.save(room);
    }

    private String buildPrivateRoomId(String sender, String receiver) {
        if (sender.compareTo(receiver) < 0) {
            return "private_" + sender + "_" + receiver;
        }
        return "private_" + receiver + "_" + sender;
    }
}
