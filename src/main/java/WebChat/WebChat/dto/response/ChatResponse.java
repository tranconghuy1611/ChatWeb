package WebChat.WebChat.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChatResponse {
    private String sender;
    private String receiver;
    private String roomId;
    private String content;
    private LocalDateTime createdAt;
}