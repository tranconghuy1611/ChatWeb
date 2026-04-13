package WebChat.WebChat.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import WebChat.WebChat.security.JwtUtil;

@Configuration
public class WebSocketAuthConfig {

    @Bean
    public ChannelInterceptor webSocketAuthChannelInterceptor(JwtUtil jwtUtil) {
        return new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

                if (StompCommand.CONNECT.equals(accessor.getCommand())) {
                    String rawAuth = accessor.getFirstNativeHeader("Authorization");
                    if (rawAuth == null) {
                        rawAuth = accessor.getFirstNativeHeader("authorization");
                    }

                    if (rawAuth == null || rawAuth.isBlank()) {
                        throw new IllegalArgumentException("Missing Authorization header for WebSocket CONNECT");
                    }

                    String token = rawAuth.startsWith("Bearer ") ? rawAuth.substring(7) : rawAuth;
                    if (!jwtUtil.validateToken(token)) {
                        throw new IllegalArgumentException("Invalid JWT token");
                    }

                    String username = jwtUtil.extractUsername(token);
                    accessor.setUser(new UsernamePasswordAuthenticationToken(username, null));
                }

                return message;
            }
        };
    }
}