package com.start.demo.api;

import com.start.demo.security.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Authentication", description = "Authentication endpoints")
public class AuthController {
    
    private final JwtUtil jwtUtil;
    
    public AuthController(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }
    
    @PostMapping("/login")
    @Operation(summary = "Login", description = "Authenticate and get JWT token")
    public Mono<ResponseEntity<LoginResponse>> login(@RequestBody LoginRequest request) {
        // Simple validation - in production, use proper user service
        if ("admin".equals(request.username()) && "admin123".equals(request.password())) {
            String token = jwtUtil.generateToken(request.username());
            return Mono.just(ResponseEntity.ok(new LoginResponse(token, "Bearer", 86400)));
        }
        return Mono.just(ResponseEntity.status(401).build());
    }
    
    public record LoginRequest(String username, String password) {}
    public record LoginResponse(String token, String type, long expiresIn) {}
}