package WebChat.WebChat.dto.request;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoomChatRequest {
    private String sender;
    private String roomId;
    private String content;
}