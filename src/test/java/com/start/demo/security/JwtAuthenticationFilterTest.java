package com.start.demo.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class JwtAuthenticationFilterTest {

        private JwtAuthenticationFilter filter;

        @Mock
        private JwtUtil jwtUtil;

        @Mock
        private WebFilterChain chain;

        @BeforeEach
        void setUp() {
                MockitoAnnotations.openMocks(this);
                filter = new JwtAuthenticationFilter(jwtUtil);
                when(chain.filter(any())).thenReturn(Mono.empty());
        }

        @Test
        void filter_WithNoAuthorizationHeader_DelegatesToChain() {
                MockServerHttpRequest request = MockServerHttpRequest
                                .get("/api/v1/employees")
                                .build();
                MockServerWebExchange exchange = MockServerWebExchange.from(request);

                StepVerifier.create(filter.filter(exchange, chain))
                                .verifyComplete();

                verify(chain).filter(exchange);
                verify(jwtUtil, never()).isTokenValid(any());
        }

        @Test
        void filter_WithNonBearerAuthorizationHeader_DelegatesToChainWithoutAuth() {
                MockServerHttpRequest request = MockServerHttpRequest
                                .get("/api/v1/employees")
                                .header(HttpHeaders.AUTHORIZATION, "Basic dXNlcjpwYXNz")
                                .build();
                MockServerWebExchange exchange = MockServerWebExchange.from(request);

                StepVerifier.create(filter.filter(exchange, chain))
                                .verifyComplete();

                verify(chain).filter(exchange);
                verify(jwtUtil, never()).isTokenValid(any());
        }

        @Test
        void filter_WithValidBearerToken_SetsAuthenticationContext() {
                String token = "valid.jwt.token";
                when(jwtUtil.isTokenValid(token)).thenReturn(true);
                when(jwtUtil.extractUsername(token)).thenReturn("testuser");

                MockServerHttpRequest request = MockServerHttpRequest
                                .get("/api/v1/employees")
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                                .build();
                MockServerWebExchange exchange = MockServerWebExchange.from(request);

                StepVerifier.create(filter.filter(exchange, chain))
                                .verifyComplete();

                verify(jwtUtil).isTokenValid(token);
                verify(jwtUtil).extractUsername(token);
                verify(chain).filter(exchange);
        }

        @Test
        void filter_WithInvalidBearerToken_DelegatesToChainWithoutAuth() {
                String token = "invalid.jwt.token";
                when(jwtUtil.isTokenValid(token)).thenReturn(false);

                MockServerHttpRequest request = MockServerHttpRequest
                                .get("/api/v1/employees")
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                                .build();
                MockServerWebExchange exchange = MockServerWebExchange.from(request);

                StepVerifier.create(filter.filter(exchange, chain))
                                .verifyComplete();

                verify(jwtUtil).isTokenValid(token);
                verify(jwtUtil, never()).extractUsername(any());
                verify(chain).filter(exchange);
        }

        @Test
        void filter_WithBearerPrefixOnly_DelegatesToChainWithoutAuth() {
                // "Bearer " with empty token
                String token = "";
                when(jwtUtil.isTokenValid(token)).thenReturn(false);

                MockServerHttpRequest request = MockServerHttpRequest
                                .get("/api/v1/employees")
                                .header(HttpHeaders.AUTHORIZATION, "Bearer ")
                                .build();
                MockServerWebExchange exchange = MockServerWebExchange.from(request);

                StepVerifier.create(filter.filter(exchange, chain))
                                .verifyComplete();

                verify(chain).filter(exchange);
        }
}
