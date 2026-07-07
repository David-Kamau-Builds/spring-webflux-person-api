package com.start.demo.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
import java.time.Instant;

@Component
public class JwtUtil {
    
    private final SecretKey key;
    private final long expiration;
    
    public JwtUtil(@Value("${jwt.secret}") String secret, 
    @Value("${jwt.expiration}") long expiration) {
        this.key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(secret));
        this.expiration = expiration;
    }
    
    public String generateToken(String username) {
        return Jwts.builder()
                .subject(username)
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(Instant.now().plusMillis(expiration)))
                .signWith(key)
                .compact();
    }
    
    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }
    
    public boolean isTokenValid(String token) {
        try {
            return !extractClaims(token).getExpiration().toInstant().isBefore(Instant.now());
        } catch (Exception e) {
            return false;
        }
    }
    
    private Claims extractClaims(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}