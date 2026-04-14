package WebChat.WebChat.config;

import WebChat.WebChat.enity.User;
import WebChat.WebChat.repository.UserRepository;
import WebChat.WebChat.security.JwtService;
import io.jsonwebtoken.Claims;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.server.HandshakeInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;

@Component
public class UserHandshakeInterceptor implements HandshakeInterceptor {
    private final JwtService jwtService;
    private final UserRepository userRepository;

    public UserHandshakeInterceptor(JwtService jwtService, UserRepository userRepository) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    @Override
    public boolean beforeHandshake(
            ServerHttpRequest request,
            ServerHttpResponse response,
            org.springframework.web.socket.WebSocketHandler wsHandler,
            Map<String, Object> attributes
    ) {

        if (request instanceof ServletServerHttpRequest servletRequest) {
            HttpServletRequest req = servletRequest.getServletRequest();
            String token = resolveToken(req);
            if (token == null || !jwtService.isTokenValid(token)) {
                return false;
            }

            Claims claims = jwtService.parseClaims(token);
            String username = claims.getSubject();
            User user = userRepository.findByUsername(username).orElse(null);
            if (user == null) {
                return false;
            }
            attributes.put("username", username);
            attributes.put("role", user.getRole());
        }

        return true;
    }

    @Override
    public void afterHandshake(
            ServerHttpRequest request,
            org.springframework.http.server.ServerHttpResponse response,
            org.springframework.web.socket.WebSocketHandler wsHandler,
            Exception exception
    ) {}

    private String resolveToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        String queryToken = request.getParameter("access_token");
        if (queryToken != null && !queryToken.isBlank()) {
            return queryToken;
        }
        return null;
    }
}