package WebChat.WebChat.websocket;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageDeliveryException;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import java.security.Principal;

@Component
public class WebSocketInboundInterceptor implements ChannelInterceptor {
    private final WebSocketSessionRegistry sessionRegistry;

    public WebSocketInboundInterceptor(WebSocketSessionRegistry sessionRegistry) {
        this.sessionRegistry = sessionRegistry;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        Principal user = accessor.getUser();
        String sessionId = accessor.getSessionId();

        if (StompCommand.CONNECT.equals(accessor.getCommand()) && user != null && sessionId != null) {
            boolean ok = sessionRegistry.register(user.getName(), sessionId);
            if (!ok) {
                throw new MessageDeliveryException("User already has an active websocket connection");
            }
        }

        if (StompCommand.DISCONNECT.equals(accessor.getCommand()) && sessionId != null) {
            sessionRegistry.unregisterBySessionId(sessionId);
        }

        return message;
    }
}
