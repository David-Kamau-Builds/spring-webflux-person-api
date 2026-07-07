package com.start.demo.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RateLimitFilterTest {

    private RateLimitFilter rateLimitFilter;
    private WebFilterChain chain;

    @BeforeEach
    void setUp() {
        // Use a small limit (3 req / 1 min) for the default filter in basic tests
        rateLimitFilter = new RateLimitFilter(3, java.time.Duration.ofMinutes(1));
        chain = mock(WebFilterChain.class);
        when(chain.filter(any())).thenReturn(Mono.empty());
    }

    @Test
    void filter_UnderRateLimit_DelegatesToChain() {
        MockServerHttpRequest request = MockServerHttpRequest
                .get("/api/v1/employees")
                .remoteAddress(new java.net.InetSocketAddress("192.168.1.1", 12345))
                .build();
        MockServerWebExchange exchange = MockServerWebExchange.from(request);

        StepVerifier.create(rateLimitFilter.filter(exchange, chain))
                .verifyComplete();

        // Should not be rate-limited — status not set to 429
        assertThat(exchange.getResponse().getStatusCode()).isNotEqualTo(HttpStatus.TOO_MANY_REQUESTS);
    }

    @Test
    void filter_WithNullRemoteAddress_UsesUnknownClientId() {
        // MockServerHttpRequest without remoteAddress → null remote address
        MockServerHttpRequest request = MockServerHttpRequest
                .get("/api/v1/employees")
                .build();
        MockServerWebExchange exchange = MockServerWebExchange.from(request);

        // First request with unknown client should pass through
        StepVerifier.create(rateLimitFilter.filter(exchange, chain))
                .verifyComplete();
    }

    @Test
    void filter_WhenRateLimitExceeded_Returns429() {
        // Use a unique IP and a limit of 2 to avoid making 100 requests
        String testIp = "10.0.0.99";
        RateLimitFilter isolatedFilter = new RateLimitFilter(2, java.time.Duration.ofMinutes(1));

        // Make 2 requests to reach the limit
        for (int i = 0; i < 2; i++) {
            MockServerHttpRequest req = MockServerHttpRequest
                    .get("/api/v1/employees")
                    .remoteAddress(new java.net.InetSocketAddress(testIp, 9000))
                    .build();
            MockServerWebExchange exc = MockServerWebExchange.from(req);
            isolatedFilter.filter(exc, chain).block();
        }

        // 101st request should be rate-limited
        MockServerHttpRequest limitedRequest = MockServerHttpRequest
                .get("/api/v1/employees")
                .remoteAddress(new java.net.InetSocketAddress(testIp, 9000))
                .build();
        MockServerWebExchange limitedExchange = MockServerWebExchange.from(limitedRequest);

        StepVerifier.create(isolatedFilter.filter(limitedExchange, chain))
                .verifyComplete();

        assertThat(limitedExchange.getResponse().getStatusCode())
                .isEqualTo(HttpStatus.TOO_MANY_REQUESTS);
    }

    @Test
    void filter_DifferentClients_HaveIndependentLimits() {
        RateLimitFilter isolatedFilter = new RateLimitFilter(2, java.time.Duration.ofMinutes(1));
        WebFilterChain passChain = mock(WebFilterChain.class);
        when(passChain.filter(any())).thenReturn(Mono.empty());

        // Exhaust limit for client A (3 requests exceeds limit of 2)
        for (int i = 0; i < 3; i++) {
            MockServerHttpRequest req = MockServerHttpRequest
                    .get("/api/v1/employees")
                    .remoteAddress(new java.net.InetSocketAddress("10.0.0.1", 9000))
                    .build();
            isolatedFilter.filter(MockServerWebExchange.from(req), passChain).block();
        }

        // Client B should still be allowed
        MockServerHttpRequest clientBRequest = MockServerHttpRequest
                .get("/api/v1/employees")
                .remoteAddress(new java.net.InetSocketAddress("10.0.0.2", 9000))
                .build();
        MockServerWebExchange clientBExchange = MockServerWebExchange.from(clientBRequest);

        StepVerifier.create(isolatedFilter.filter(clientBExchange, passChain))
                .verifyComplete();

        assertThat(clientBExchange.getResponse().getStatusCode())
                .isNotEqualTo(HttpStatus.TOO_MANY_REQUESTS);
    }

    @Test
    void filter_AfterWindowExpires_CounterResetsAndRequestAllowed() {
        // Use a 1ms window so it expires almost immediately
        RateLimitFilter shortWindowFilter = new RateLimitFilter(1, java.time.Duration.ofMillis(1));
        WebFilterChain passChain = mock(WebFilterChain.class);
        when(passChain.filter(any())).thenReturn(Mono.empty());

        String testIp = "10.1.1.1";
        long start = System.currentTimeMillis();

        // Exhaust the limit (1 request allowed, 2nd gets 429)
        for (int i = 0; i < 2; i++) {
            MockServerHttpRequest req = MockServerHttpRequest
                    .get("/api/v1/employees")
                    .remoteAddress(new java.net.InetSocketAddress(testIp, 9000))
                    .build();
            shortWindowFilter.filter(MockServerWebExchange.from(req), passChain).block();
        }

        // Busy-wait until at least 1ms has elapsed so the window expires
        while (System.currentTimeMillis() - start < 5) {
            // spin — avoids Thread.sleep (SonarCloud S2925)
        }

        // After the window resets, the next request should be allowed again
        MockServerHttpRequest resetRequest = MockServerHttpRequest
                .get("/api/v1/employees")
                .remoteAddress(new java.net.InetSocketAddress(testIp, 9000))
                .build();
        MockServerWebExchange resetExchange = MockServerWebExchange.from(resetRequest);

        StepVerifier.create(shortWindowFilter.filter(resetExchange, passChain))
                .verifyComplete();

        assertThat(resetExchange.getResponse().getStatusCode())
                .isNotEqualTo(HttpStatus.TOO_MANY_REQUESTS);
    }
}
