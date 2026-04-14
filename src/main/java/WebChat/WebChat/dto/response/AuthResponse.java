package WebChat.WebChat.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthResponse {
    private Integer id;
    private String username;
    private String fullname;
    private String role;
    private String accessToken;
}
