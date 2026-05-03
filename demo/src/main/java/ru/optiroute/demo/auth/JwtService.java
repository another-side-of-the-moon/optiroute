package ru.optiroute.demo.auth;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import ru.optiroute.demo.config.AppProperties;

@Service
public class JwtService {

    private final SecretKey secretKey;
    private final long jwtExpiration;

    public JwtService(AppProperties appProperties) {
        this.secretKey = buildKey(appProperties.getSecurity().getJwtSecret());
        this.jwtExpiration = appProperties.getSecurity().getJwtExpiration();
    }

    public String generateToken(AuthenticatedUser user) {
        var now = Instant.now();
        return Jwts.builder()
                .subject(user.getUsername())
                .claim("uid", user.getId())
                .claim("name", user.getName())
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusMillis(jwtExpiration)))
                .signWith(secretKey)
                .compact();
    }

    public String extractUsername(String token) {
        return parseClaims(token).getSubject();
    }

    public boolean isValid(String token, AuthenticatedUser user) {
        var claims = parseClaims(token);
        return claims.getSubject().equalsIgnoreCase(user.getUsername())
                && claims.getExpiration().after(new Date());
    }

    private Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey buildKey(String secret) {
        try {
            return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
        } catch (RuntimeException exception) {
            var bytes = secret.getBytes(StandardCharsets.UTF_8);
            if (bytes.length >= 32) {
                return Keys.hmacShaKeyFor(bytes);
            }
            var padded = new byte[32];
            System.arraycopy(bytes, 0, padded, 0, bytes.length);
            for (int index = bytes.length; index < padded.length; index++) {
                padded[index] = (byte) ('a' + (index % 26));
            }
            return Keys.hmacShaKeyFor(padded);
        }
    }
}

