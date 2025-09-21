package com.start.demo.api;

import com.start.demo.health.DatabaseHealthIndicator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/health")
@Tag(name = "Health Check", description = "Application health endpoints")
public class HealthController {
    
    private final DatabaseHealthIndicator healthIndicator;
    
    public HealthController(DatabaseHealthIndicator healthIndicator) {
        this.healthIndicator = healthIndicator;
    }
    
    @GetMapping
    @Operation(summary = "Health check", description = "Check application and database health")
    public Mono<HealthResponse> health() {
        return healthIndicator.checkHealth()
            .map(dbStatus -> new HealthResponse("UP", dbStatus, System.currentTimeMillis()));
    }
    
    public record HealthResponse(String status, String database, long timestamp) {}
}