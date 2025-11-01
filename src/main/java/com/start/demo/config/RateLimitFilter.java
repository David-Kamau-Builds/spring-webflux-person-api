package com.start.demo.config;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class RateLimitFilter implements WebFilter {
    
    private final ConcurrentMap<String, RequestCounter> requests = new ConcurrentHashMap<>();
    private final int maxRequests = 100; // per minute
    private static final Duration window = Duration.ofMinutes(1);
    
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String clientId = getClientId(exchange);
        
        if (isRateLimited(clientId)) {
            exchange.getResponse().setStatusCode(HttpStatus.TOO_MANY_REQUESTS);
            return exchange.getResponse().setComplete();
        }
        
        return chain.filter(exchange);
    }
    
    private String getClientId(ServerWebExchange exchange) {
        return exchange.getRequest().getRemoteAddress() != null 
            ? exchange.getRequest().getRemoteAddress().getAddress().getHostAddress()
            : "unknown";
    }
    
    private boolean isRateLimited(String clientId) {
        long now = System.currentTimeMillis();
        RequestCounter counter = requests.computeIfAbsent(clientId, k -> new RequestCounter());
        
        // Clean old entries
        if (now - counter.windowStart.get() > window.toMillis()) {
            counter.count.set(0);
            counter.windowStart.set(now);
        }
        
        return counter.count.incrementAndGet() > maxRequests;
    }
    
    private static class RequestCounter {
        AtomicInteger count = new AtomicInteger(0);
        AtomicLong windowStart = new AtomicLong(System.currentTimeMillis());
    }
}