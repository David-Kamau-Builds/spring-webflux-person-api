package com.start.demo.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/api/v1/metrics")
@Tag(name = "Metrics", description = "Application metrics endpoints")
public class MetricsController {
    
    private final AtomicLong employeeCreatedCounter;
    private final AtomicLong employeeSearchCounter;
    
    public MetricsController(AtomicLong employeeCreatedCounter, AtomicLong employeeSearchCounter) {
        this.employeeCreatedCounter = employeeCreatedCounter;
        this.employeeSearchCounter = employeeSearchCounter;
    }
    
    @GetMapping
    @Operation(summary = "Get metrics", description = "Get application metrics")
    public Mono<MetricsResponse> getMetrics() {
        return Mono.just(new MetricsResponse(
            employeeCreatedCounter.get(),
            employeeSearchCounter.get(),
            System.currentTimeMillis()
        ));
    }
    
    public record MetricsResponse(long employeesCreated, long searchesPerformed, long timestamp) {}
}