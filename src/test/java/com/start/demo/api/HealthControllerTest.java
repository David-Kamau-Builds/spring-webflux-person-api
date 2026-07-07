package com.start.demo.api;

import com.start.demo.health.DatabaseHealthIndicator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.when;

class HealthControllerTest {

    @InjectMocks
    private HealthController healthController;

    @Mock
    private DatabaseHealthIndicator healthIndicator;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void health_WhenDatabaseIsHealthy_ReturnsUpStatus() {
        when(healthIndicator.checkHealth())
                .thenReturn(Mono.just("Database healthy - 10 employees"));

        StepVerifier.create(healthController.health())
                .expectNextMatches(response ->
                        "UP".equals(response.status())
                        && "Database healthy - 10 employees".equals(response.database())
                        && response.timestamp() > 0
                )
                .verifyComplete();
    }

    @Test
    void health_WhenDatabaseIsDown_ReturnsUpStatusWithFailureMessage() {
        when(healthIndicator.checkHealth())
                .thenReturn(Mono.just("Database connection failed"));

        StepVerifier.create(healthController.health())
                .expectNextMatches(response ->
                        "UP".equals(response.status())
                        && "Database connection failed".equals(response.database())
                )
                .verifyComplete();
    }
}
