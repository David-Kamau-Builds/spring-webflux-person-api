package com.start.demo.service;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
public class CacheService {
    
    private final ConcurrentMap<String, CacheEntry> cache = new ConcurrentHashMap<>();
    private final Duration ttl = Duration.ofMinutes(5);
    
    public <T> Mono<T> get(String key, Class<T> type) {
        return Mono.fromCallable(() -> {
            CacheEntry entry = cache.get(key);
            if (entry != null && !entry.isExpired()) {
                return type.cast(entry.getValue());
            }
            cache.remove(key);
            return null;
        });
    }
    
    public <T> Mono<Void> put(String key, T value) {
        return Mono.fromRunnable(() -> 
            cache.put(key, new CacheEntry(value, System.currentTimeMillis() + ttl.toMillis()))
        );
    }
    
    private static class CacheEntry {
        private final Object value;
        private final long expiryTime;
        
        CacheEntry(Object value, long expiryTime) {
            this.value = value;
            this.expiryTime = expiryTime;
        }
        
        Object getValue() { return value; }
        boolean isExpired() { return System.currentTimeMillis() > expiryTime; }
    }
}