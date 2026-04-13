package WebChat.WebChat.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    // ❗ SECRET phải đủ dài (>= 32 ký tự)
    private final String SECRET = "my_super_secret_key_12345678901234567890123456789012345678901234567890";

    private final long EXPIRATION = 1000 * 60 * 60 * 24;

    // 🔥 TẠO KEY CHUẨN
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }

    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    public String extractUsername(String token) {
        return getClaims(token).getSubject();
    }

    public boolean validateToken(String token) {
        try {
            getClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey()) // ❗ parserBuilder (mới)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}