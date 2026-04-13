package WebChat.WebChat.controller;


import WebChat.WebChat.dto.request.ChatMessageDTO;
import WebChat.WebChat.enity.Message;
import WebChat.WebChat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class ChatWebSocketController {

    private final ChatService chatService;
    private final SimpMessagingTemplate messagingTemplate;

    // =========================
    // HANDLE MESSAGE
    // =========================
    @MessageMapping("/chat")
    public void handleMessage(ChatMessageDTO dto, Principal principal) {
        if (principal == null || principal.getName() == null || principal.getName().isBlank()) {
            throw new RuntimeException("Unauthenticated websocket session");
        }

        String senderUsername = principal.getName();

        if ("PRIVATE".equals(dto.getType())) {
            handlePrivateMessage(dto, senderUsername);
        } else {
            handleGroupMessage(dto, senderUsername);
        }
    }

    // =========================
    // PRIVATE CHAT
    // =========================
    private void handlePrivateMessage(ChatMessageDTO dto, String senderUsername) {
        if (dto.getReceiver() == null || dto.getReceiver().isBlank()) {
            throw new RuntimeException("Receiver is required for private message");
        }

        Message message = chatService.sendPrivateMessage(
                senderUsername,
                dto.getReceiver(),
                dto.getContent()
        );

        // gửi cho receiver
        // gửi cho receiver
        messagingTemplate.convertAndSendToUser(dto.getReceiver(), "/queue/messages", message);
        messagingTemplate.convertAndSendToUser(senderUsername, "/queue/messages", message);
    }

    // =========================
    // GROUP CHAT
    // =========================
    private void handleGroupMessage(ChatMessageDTO dto, String senderUsername) {
        if (dto.getRoomId() == null || dto.getRoomId().isBlank()) {
            throw new RuntimeException("roomId is required for group message");
        }

        Message message = chatService.sendGroupMessage(
                senderUsername,
                dto.getRoomId(),
                dto.getContent()
        );

        messagingTemplate.convertAndSend(
                "/topic/room/" + dto.getRoomId(),
                message
        );
    }
}