package WebChat.WebChat.service;

import WebChat.WebChat.enity.Message;
import WebChat.WebChat.enity.Room;
import WebChat.WebChat.enity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import WebChat.WebChat.repository.MessageRepository;
import WebChat.WebChat.repository.RoomRepository;
import WebChat.WebChat.repository.UserRepository;

import java.util.List;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private RoomRepository roomRepo;

    // chat riêng
    public Message savePrivate(String sender, String receiver, String content) {

        User s = userRepo.findByUsername(sender).get();
        User r = userRepo.findByUsername(receiver).get();

        Message m = new Message();
        m.setSender(s);
        m.setReceiver(r);
        m.setContent(content);

        return messageRepo.save(m);
    }

    // chat nhóm
    public Message saveRoom(String sender, String roomId, String content) {

        User s = userRepo.findByUsername(sender).get();
        Room room = roomRepo.findById(roomId).get();

        Message m = new Message();
        m.setSender(s);
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
}
