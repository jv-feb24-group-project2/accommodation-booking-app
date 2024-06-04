package ua.rent.masters.easystay.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.function.Function;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

    @Value("${jwt.expiration}")
    private long expiration;

    private final SecretKey secret;

    public JwtUtil(@Value("${jwt.secret}") String secretString) {
        secret = Keys.hmacShaKeyFor(secretString.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(String username) {
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(secret)
                .compact();

    }

    public boolean isValidToken(String token) {
        try {
            Jws<Claims> claimsJws = Jwts.parser()
                    .verifyWith(secret)
                    .build()
                    .parseSignedClaims(token);
            return !claimsJws.getPayload().getExpiration().before(new Date());
        } catch (JwtException jwtException) {
            throw new JwtException("Expired JWT token : " + token);
        } catch (IllegalArgumentException illegalArgumentException) {
            throw new IllegalArgumentException("Invalid JWT token: " + token);
        }
    }

    public String getUsername(String token) {
        return getClaimsFromToken(token, Claims::getSubject);
    }

    private <T> T getClaimsFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = Jwts.parser()
                .verifyWith(secret)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claimsResolver.apply(claims);
    }
}
