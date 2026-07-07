package com.start.demo.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
    }

    @Test
    void handleValidationException_ReturnsFieldErrorMessage() {
        WebExchangeBindException ex = mock(WebExchangeBindException.class);
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError = new FieldError("employee", "email", "Email is required");

        when(ex.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError));

        StepVerifier.create(handler.handleValidationException(ex))
                .expectNextMatches(response ->
                        response.getStatusCodeValue() == 400
                        && "Email is required".equals(response.getBody().get("error"))
                )
                .verifyComplete();
    }

    @Test
    void handleGenericException_Returns500WithGenericMessage() {
        Exception ex = new RuntimeException("Something went wrong");

        StepVerifier.create(handler.handleGenericException(ex))
                .expectNextMatches(response ->
                        response.getStatusCodeValue() == 500
                        && "Internal server error".equals(response.getBody().get("error"))
                )
                .verifyComplete();
    }
}
