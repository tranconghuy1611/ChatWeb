package WebChat.WebChat.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.web.socket.config.annotation.*;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final ChannelInterceptor webSocketAuthChannelInterceptor;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {

        // nơi client SUBSCRIBE
        config.enableSimpleBroker("/topic", "/queue");

        // nơi client SEND
        config.setApplicationDestinationPrefixes("/app");

        // private message
        config.setUserDestinationPrefix("/user");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOrigins("http://localhost:5173") // 🔥 FIX
                .withSockJS(); // hỗ trợ fallback
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(webSocketAuthChannelInterceptor);
    }
}