package WebChat.WebChat.websocket;

import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Component
public class WebSocketSessionRegistry {
    private final ConcurrentHashMap<String, String> userSessions = new ConcurrentHashMap<>();

    public boolean register(String username, String sessionId) {
        String existing = userSessions.putIfAbsent(username, sessionId);
        return existing == null || existing.equals(sessionId);
    }

    public void unregisterBySessionId(String sessionId) {
        userSessions.entrySet().removeIf(entry -> entry.getValue().equals(sessionId));
    }
}
