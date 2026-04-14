package WebChat.WebChat.config;

import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.Map;

class StompPrincipal implements Principal {
    private final String name;

    StompPrincipal(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}

public class CustomHandshakeHandler extends DefaultHandshakeHandler {

    @Override
    protected Principal determineUser(
            org.springframework.http.server.ServerHttpRequest request,
            org.springframework.web.socket.WebSocketHandler wsHandler,
            Map<String, Object> attributes
    ) {

        String username = (String) attributes.get("username");
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Unauthenticated websocket user");
        }

        return new StompPrincipal(username);
    }
}