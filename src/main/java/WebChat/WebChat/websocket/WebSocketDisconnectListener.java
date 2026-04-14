package WebChat.WebChat.websocket;

import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
public class WebSocketDisconnectListener {
    private final WebSocketSessionRegistry sessionRegistry;

    public WebSocketDisconnectListener(WebSocketSessionRegistry sessionRegistry) {
        this.sessionRegistry = sessionRegistry;
    }

    @EventListener
    public void onDisconnect(SessionDisconnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        if (accessor.getSessionId() != null) {
            sessionRegistry.unregisterBySessionId(accessor.getSessionId());
        }
    }
}
