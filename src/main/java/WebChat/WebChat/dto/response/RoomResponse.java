package WebChat.WebChat.dto.response;

import lombok.Getter;

@Getter
public class RoomResponse {
    private String roomId;
    private String roomName;

    public RoomResponse(String roomId, String roomName) {
        this.roomId = roomId;
        this.roomName = roomName;
    }

    // getter
}