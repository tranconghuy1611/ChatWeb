package WebChat.WebChat.dto.request;

import lombok.Data;

@Data
public class PrivateRoomRequest {
    private String targetUsername;
}