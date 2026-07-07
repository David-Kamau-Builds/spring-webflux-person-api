package com.start.demo.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

class CacheServiceTest {

    private CacheService cacheService;

    @BeforeEach
    void setUp() {
        cacheService = new CacheService();
    }

    @Test
    void get_WhenKeyNotPresent_ReturnsEmpty() {
        StepVerifier.create(cacheService.get("missing-key", String.class))
                .verifyComplete();
    }

    @Test
    void put_ThenGet_ReturnsCachedValue() {
        String key = "test-key";
        String value = "test-value";

        StepVerifier.create(
                cacheService.put(key, value)
                        .then(cacheService.get(key, String.class))
        )
                .expectNext(value)
                .verifyComplete();
    }

    @Test
    void put_ThenGet_WithDifferentType_ReturnsValue() {
        String key = "int-key";
        Integer value = 42;

        StepVerifier.create(
                cacheService.put(key, value)
                        .then(cacheService.get(key, Integer.class))
        )
                .expectNext(value)
                .verifyComplete();
    }

    @Test
    void put_OverwritesExistingValue() {
        String key = "overwrite-key";

        StepVerifier.create(
                cacheService.put(key, "first")
                        .then(cacheService.put(key, "second"))
                        .then(cacheService.get(key, String.class))
        )
                .expectNext("second")
                .verifyComplete();
    }

    @Test
    void get_DifferentKeys_AreIndependent() {
        StepVerifier.create(
                cacheService.put("key1", "value1")
                        .then(cacheService.put("key2", "value2"))
                        .thenMany(
                                cacheService.get("key1", String.class)
                        )
        )
                .expectNext("value1")
                .verifyComplete();
    }
}
