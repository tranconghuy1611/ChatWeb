package WebChat.WebChat.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PrivateChatRequest {
    private String sender;
    private String receiver;
    private String content;

    // getter setter
}
