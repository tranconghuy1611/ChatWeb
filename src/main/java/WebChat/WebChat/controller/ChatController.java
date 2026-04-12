package WebChat.WebChat.controller;

import WebChat.WebChat.dto.request.PrivateChatRequest;
import WebChat.WebChat.dto.request.RoomChatRequest;
import WebChat.WebChat.dto.response.ChatResponse;
import WebChat.WebChat.enity.ChatMessage;
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
    public void privateChat(PrivateChatRequest req) {

        Message saved = messageService.savePrivate(
                req.getSender(),
                req.getReceiver(),
                req.getContent()
        );

        ChatResponse res = new ChatResponse(
                saved.getSender().getUsername(),
                saved.getContent(),                // ✅ đúng
                saved.getReceiver().getUsername()
        );

        messagingTemplate.convertAndSendToUser(
                req.getReceiver(),
                "/queue/messages",
                res
        );

        messagingTemplate.convertAndSendToUser(
                req.getSender(),
                "/queue/messages",
                res
        );
    }


    @MessageMapping("/chat.room")
    public void roomChat(RoomChatRequest req) {
        Message saved = messageService.saveRoom(
                req.getSender(),
                req.getRoomId(),
                req.getContent()
        );

        messagingTemplate.convertAndSend(
                "/topic/room/" + req.getRoomId(),
                saved
        );
    }
}
