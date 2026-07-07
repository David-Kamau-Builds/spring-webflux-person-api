package com.start.demo.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import java.util.concurrent.atomic.AtomicLong;

class MetricsControllerTest {

    private MetricsController metricsController;

    @BeforeEach
    void setUp() {
        metricsController = new MetricsController(new AtomicLong(0), new AtomicLong(0));
    }

    @Test
    void getMetrics_ReturnsZeroCountersInitially() {
        StepVerifier.create(metricsController.getMetrics())
                .expectNextMatches(response ->
                        response.employeesCreated() == 0
                        && response.searchesPerformed() == 0
                        && response.timestamp() > 0
                )
                .verifyComplete();
    }

    @Test
    void getMetrics_ReflectsCounterValues() {
        AtomicLong created = new AtomicLong(42);
        AtomicLong searches = new AtomicLong(7);
        metricsController = new MetricsController(created, searches);

        StepVerifier.create(metricsController.getMetrics())
                .expectNextMatches(response ->
                        response.employeesCreated() == 42
                        && response.searchesPerformed() == 7
                )
                .verifyComplete();
    }
}
