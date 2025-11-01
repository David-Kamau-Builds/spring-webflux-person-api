package com.start.demo.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.core.publisher.Mono;

@RestControllerAdvice
public class GlobalExceptionHandler {

  private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

  @ExceptionHandler(WebExchangeBindException.class)
  public Mono<ResponseEntity<Map<String, String>>> handleValidationException(
      WebExchangeBindException ex) {
    String error = ex.getBindingResult().getFieldErrors().get(0).getDefaultMessage();
    return Mono.just(ResponseEntity.badRequest().body(Map.of("error", error)));
  }

  @ExceptionHandler(Exception.class)
  public Mono<ResponseEntity<Map<String, String>>> handleGenericException(Exception ex) {
    log.error("An unexpected error occurred", ex);
    return Mono.just(
        ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(Map.of("error", "Internal server error")));
  }
}