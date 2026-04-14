package WebChat.WebChat.dto.request;

import lombok.Getter;

import java.util.List;

@Getter
public class RoomMembersRequest {
    private List<String> usernames;
}
