package com.start.demo.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    private JwtUtil jwtUtil;
    private final String testSecret = "dGVzdC1zZWNyZXQtZm9yLWp3dC10b2tlbi1nZW5lcmF0aW9uLWFuZC12YWxpZGF0aW9u";
    private final long testExpiration = 86400000L; // 24 hours

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil(testSecret, testExpiration);
    }

    @Test
    void generateToken_Success() {
        String username = "testuser";
        String token = jwtUtil.generateToken(username);

        assertNotNull(token);
        assertFalse(token.isEmpty());
        assertTrue(token.contains("."));
    }

    @Test
    void extractUsername_Success() {
        String username = "testuser";
        String token = jwtUtil.generateToken(username);

        String extractedUsername = jwtUtil.extractUsername(token);

        assertEquals(username, extractedUsername);
    }

    @Test
    void isTokenValid_ValidToken_ReturnsTrue() {
        String username = "testuser";
        String token = jwtUtil.generateToken(username);

        boolean isValid = jwtUtil.isTokenValid(token);

        assertTrue(isValid);
    }

    @Test
    void isTokenValid_InvalidToken_ReturnsFalse() {
        String invalidToken = "invalid.token.here";

        boolean isValid = jwtUtil.isTokenValid(invalidToken);

        assertFalse(isValid);
    }

    @Test
    void isTokenValid_EmptyToken_ReturnsFalse() {
        boolean isValid = jwtUtil.isTokenValid("");

        assertFalse(isValid);
    }

    @Test
    void isTokenValid_NullToken_ReturnsFalse() {
        boolean isValid = jwtUtil.isTokenValid(null);

        assertFalse(isValid);
    }

    @Test
    void extractUsername_InvalidToken_ThrowsException() {
        String invalidToken = "invalid.token.here";

        assertThrows(Exception.class, () -> jwtUtil.extractUsername(invalidToken));
    }
}