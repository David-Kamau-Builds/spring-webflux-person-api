package com.start.demo.health;

import com.start.demo.repository.EmployeeRepository;
import org.springframework.boot.actuator.health.Health;
import org.springframework.boot.actuator.health.ReactiveHealthIndicator;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class DatabaseHealthIndicator implements ReactiveHealthIndicator {
    
    private final EmployeeRepository employeeRepository;
    
    public DatabaseHealthIndicator(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }
    
    @Override
    public Mono<Health> health() {
        return employeeRepository.count()
                .map(count -> Health.up()
                        .withDetail("database", "H2")
                        .withDetail("employeeCount", count)
                        .withDetail("status", "Connected")
                        .build())
                .onErrorReturn(Health.down()
                        .withDetail("database", "H2")
                        .withDetail("status", "Connection failed")
                        .build());
    }
}