package com.start.demo.api;

import com.start.demo.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class AuthControllerTest {

    @InjectMocks
    private AuthController authController;

    @Mock
    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // Manually set fields since @Value won't work without Spring context
        authController = new AuthController(jwtUtil, "admin", "password");
    }

    @Test
    void login_WithValidCredentials_ReturnsTokenResponse() {
        when(jwtUtil.generateToken(anyString())).thenReturn("mock-jwt-token");

        var request = new AuthController.LoginRequest("admin", "password");

        StepVerifier.create(authController.login(request))
                .expectNextMatches(response ->
                        response.getStatusCode() == HttpStatus.OK
                        && response.getBody() != null
                        && "mock-jwt-token".equals(response.getBody().token())
                        && "Bearer".equals(response.getBody().type())
                        && response.getBody().expiresIn() == 86400
                )
                .verifyComplete();
    }

    @Test
    void login_WithWrongPassword_Returns401() {
        var request = new AuthController.LoginRequest("admin", "wrong-password");

        StepVerifier.create(authController.login(request))
                .expectNextMatches(response -> response.getStatusCode() == HttpStatus.UNAUTHORIZED)
                .verifyComplete();
    }

    @Test
    void login_WithWrongUsername_Returns401() {
        var request = new AuthController.LoginRequest("unknown", "password");

        StepVerifier.create(authController.login(request))
                .expectNextMatches(response -> response.getStatusCode() == HttpStatus.UNAUTHORIZED)
                .verifyComplete();
    }

    @Test
    void login_WithBothWrong_Returns401() {
        var request = new AuthController.LoginRequest("wrong", "wrong");

        StepVerifier.create(authController.login(request))
                .expectNextMatches(response -> response.getStatusCode() == HttpStatus.UNAUTHORIZED)
                .verifyComplete();
    }
}
