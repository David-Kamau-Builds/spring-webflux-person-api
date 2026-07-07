package com.start.demo.health;

import com.start.demo.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.when;

class DatabaseHealthIndicatorTest {

    @InjectMocks
    private DatabaseHealthIndicator databaseHealthIndicator;

    @Mock
    private EmployeeRepository employeeRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void checkHealth_WhenDatabaseIsAccessible_ReturnsHealthyMessage() {
        when(employeeRepository.count()).thenReturn(Mono.just(5L));

        StepVerifier.create(databaseHealthIndicator.checkHealth())
                .expectNext("Database healthy - 5 employees")
                .verifyComplete();
    }

    @Test
    void checkHealth_WhenZeroEmployees_ReturnsHealthyWithZeroCount() {
        when(employeeRepository.count()).thenReturn(Mono.just(0L));

        StepVerifier.create(databaseHealthIndicator.checkHealth())
                .expectNext("Database healthy - 0 employees")
                .verifyComplete();
    }

    @Test
    void checkHealth_WhenDatabaseThrowsError_ReturnsFallbackMessage() {
        when(employeeRepository.count())
                .thenReturn(Mono.error(new RuntimeException("Connection refused")));

        StepVerifier.create(databaseHealthIndicator.checkHealth())
                .expectNext("Database connection failed")
                .verifyComplete();
    }
}
