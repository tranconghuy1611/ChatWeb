package WebChat.WebChat.controller;

import WebChat.WebChat.dto.response.ChatResponse;
import WebChat.WebChat.enity.Message;
import WebChat.WebChat.repository.RoomMemberRepository;
import WebChat.WebChat.service.MessageService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/messages")
public class MessageController {
    private final MessageService messageService;
    private final RoomMemberRepository roomMemberRepository;

    public MessageController(MessageService messageService, RoomMemberRepository roomMemberRepository) {
        this.messageService = messageService;
        this.roomMemberRepository = roomMemberRepository;
    }

    @GetMapping("/rooms/{roomId}")
    public List<ChatResponse> getRoomHistory(
            @PathVariable String roomId,
            Principal principal,
            Authentication authentication
    ) {
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
        boolean isMember = roomMemberRepository.existsByRoom_RoomIdAndUser_Username(roomId, principal.getName());
        if (!isAdmin && !isMember) {
            throw new RuntimeException("Bạn không có quyền xem lịch sử room này");
        }

        List<Message> messages = messageService.getRoomChat(roomId);
        return messages.stream()
                .map(message -> new ChatResponse(
                        message.getSender().getUsername(),
                        message.getReceiver() != null ? message.getReceiver().getUsername() : null,
                        message.getRoom() != null ? message.getRoom().getRoomId() : null,
                        message.getContent(),
                        message.getCreatedAt()
                ))
                .toList();
    }
}
