package com.start.demo.exception;

import java.util.Map;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.core.publisher.Mono;

@RestControllerAdvice
public class GlobalExceptionHandler {

  private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

  @ExceptionHandler(WebExchangeBindException.class)
  public Mono<ResponseEntity<Map<String, Object>>> handleValidationException(
      WebExchangeBindException ex) {
    String errors =
        ex.getBindingResult().getFieldErrors().stream()
            .map(error -> error.getField() + ": " + error.getDefaultMessage())
            .collect(Collectors.joining(", "));

    logger.warn("Validation error: {}", errors);
    return Mono.just(
        ResponseEntity.badRequest().body(Map.of("message", "Validation failed: " + errors)));
  }

  @ExceptionHandler(Exception.class)
  public Mono<ResponseEntity<Map<String, Object>>> handleGenericException(Exception ex) {
    logger.error("Unexpected error occurred", ex);
    return Mono.just(
        ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(Map.of("message", "An internal server error occurred")));
  }
}
