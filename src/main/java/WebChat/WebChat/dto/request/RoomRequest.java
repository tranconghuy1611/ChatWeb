package WebChat.WebChat.dto.request;


import lombok.Getter;

import java.util.List;
@Getter
public class RoomRequest {
    private String roomId;
    private String roomName;
    private List<String> users;
}
