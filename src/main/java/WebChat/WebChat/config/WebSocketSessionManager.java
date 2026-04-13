package WebChat.WebChat.config;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class WebSocketSessionManager {

    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    public void add(String username, WebSocketSession session) {
        sessions.put(username, session);
    }

    public WebSocketSession get(String username) {
        return sessions.get(username);
    }

    public void remove(String username) {
        sessions.remove(username);
    }
}