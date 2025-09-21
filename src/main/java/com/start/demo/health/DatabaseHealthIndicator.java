package com.start.demo.health;

import com.start.demo.repository.EmployeeRepository;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class DatabaseHealthIndicator {
    
    private final EmployeeRepository employeeRepository;
    
    public DatabaseHealthIndicator(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }
    
    public Mono<String> checkHealth() {
        return employeeRepository.count()
                .map(count -> "Database healthy - " + count + " employees")
                .onErrorReturn("Database connection failed");
    }
}