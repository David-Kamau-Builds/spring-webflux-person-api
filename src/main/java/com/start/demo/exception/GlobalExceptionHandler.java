package com.start.demo.exception;

import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.core.publisher.Mono;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(WebExchangeBindException.class)
  public Mono<ResponseEntity<Map<String, String>>> handleValidationException(
      WebExchangeBindException ex) {
    String error = ex.getBindingResult().getFieldErrors().get(0).getDefaultMessage();
    return Mono.just(ResponseEntity.badRequest().body(Map.of("error", error)));
  }

  @ExceptionHandler(Exception.class)
  public Mono<ResponseEntity<Map<String, String>>> handleGenericException(Exception ex) {
    return Mono.just(
        ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(Map.of("error", "Internal server error")));
  }
}