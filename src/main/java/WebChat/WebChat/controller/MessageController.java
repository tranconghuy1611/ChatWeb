package WebChat.WebChat.controller;


import WebChat.WebChat.enity.Message;
import WebChat.WebChat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {

    private final ChatService chatService;

    // chat riêng
    @GetMapping("/private")
    public List<Message> getPrivateMessages(
            @RequestParam String user1,
            @RequestParam String user2
    ) {
        return chatService.getPrivateMessages(user1, user2);
    }

    // chat nhóm
    @GetMapping("/room/{roomId}")
    public List<Message> getRoomMessages(@PathVariable String roomId) {
        return chatService.getRoomMessages(roomId);
    }
}