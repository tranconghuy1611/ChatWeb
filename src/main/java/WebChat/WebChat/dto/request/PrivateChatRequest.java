package WebChat.WebChat.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PrivateChatRequest {
    private String receiver;
    private String roomId;
    private String content;
}
