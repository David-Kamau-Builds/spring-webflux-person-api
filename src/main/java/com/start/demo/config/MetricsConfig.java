package com.start.demo.config;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MetricsConfig {
    
    @Bean
    public Counter employeeCreatedCounter(MeterRegistry meterRegistry) {
        return Counter.builder("employees.created")
                .description("Number of employees created")
                .register(meterRegistry);
    }
    
    @Bean
    public Timer employeeSearchTimer(MeterRegistry meterRegistry) {
        return Timer.builder("employees.search.duration")
                .description("Employee search duration")
                .register(meterRegistry);
    }
}