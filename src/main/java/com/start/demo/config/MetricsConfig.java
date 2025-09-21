package com.start.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.concurrent.atomic.AtomicLong;

@Configuration
public class MetricsConfig {
    
    @Bean
    public AtomicLong employeeCreatedCounter() {
        return new AtomicLong(0);
    }
    
    @Bean
    public AtomicLong employeeSearchCounter() {
        return new AtomicLong(0);
    }
}