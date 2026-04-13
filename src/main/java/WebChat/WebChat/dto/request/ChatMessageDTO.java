package WebChat.WebChat.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatMessageDTO {
    private String sender;
    private String receiver;
    private String roomId;
    private String content;
    private String type;
}
