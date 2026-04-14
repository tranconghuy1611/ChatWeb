package WebChat.WebChat.controller;

import WebChat.WebChat.dto.request.PrivateChatRequest;
import WebChat.WebChat.dto.request.RoomChatRequest;
import WebChat.WebChat.dto.response.ChatResponse;
import WebChat.WebChat.enity.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import WebChat.WebChat.service.MessageService;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
public class ChatController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private MessageService messageService;

    // =========================
    // CHAT RIÊNG
    // =========================
    @MessageMapping("/chat.private")
    public void privateChat(PrivateChatRequest req, Principal principal) {
        String sender = principal.getName();

        Message saved = messageService.savePrivateInRoom(
                sender,
                req.getReceiver(),
                req.getRoomId(),
                req.getContent()
        );

        ChatResponse res = new ChatResponse(
                saved.getSender().getUsername(),
                saved.getReceiver().getUsername(),
                saved.getRoom().getRoomId(),
                saved.getContent(),
                saved.getCreatedAt()
        );

        messagingTemplate.convertAndSend(
                "/topic/room/" + saved.getRoom().getRoomId(),
                res
        );
    }


    @MessageMapping("/chat.room")
    public void roomChat(RoomChatRequest req, Principal principal) {
        if (req.getRoomId() == null || req.getRoomId().isBlank()) {
            throw new IllegalArgumentException("room_id là bắt buộc khi gửi tin nhắn");
        }
        String sender = principal.getName();
        Message saved = messageService.saveRoom(
                sender,
                req.getRoomId(),
                req.getContent()
        );

        ChatResponse res = new ChatResponse(
                saved.getSender().getUsername(),
                null,
                saved.getRoom().getRoomId(),
                saved.getContent(),
                saved.getCreatedAt()
        );

        messagingTemplate.convertAndSend(
                "/topic/room/" + req.getRoomId(),
                res
        );
    }
}
