package WebChat.WebChat.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class CreateRoomRequest {
    private String roomName;
    private String creator;
    private List<String> members;
}