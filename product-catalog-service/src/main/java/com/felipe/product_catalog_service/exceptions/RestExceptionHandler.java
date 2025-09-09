package com.felipe.product_catalog_service.exceptions;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;

import io.micrometer.core.instrument.MeterRegistry;

@RestControllerAdvice
public class RestExceptionHandler {

    private final MeterRegistry meterRegistry;

    public RestExceptionHandler(MeterRegistry meterRegistry){
        this.meterRegistry = meterRegistry;
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleProductNotFoundException(ProductNotFoundException ex) {
       
        meterRegistry.counter("products.notfound").increment();
       
        Map<String, Object> errorDetails = Map.of(
                "timestamp", Instant.now(),
                "status", HttpStatus.NOT_FOUND.value(),
                "error", "Not Found",
                "message", ex.getMessage());
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(WebExchangeBindException.class)
    public ResponseEntity<Map<String,Object>> handleValidationExceptions(WebExchangeBindException ex){
        List<String> errors = ex.getBindingResult()
            .getAllErrors().stream()
            .map(error -> error.getDefaultMessage())
            .collect(Collectors.toList());
        
        Map<String, Object> errorDetails = Map.of(
            "timestamp", Instant.now(),
            "status", HttpStatus.BAD_REQUEST.value(),
            "error", "Bad Request",
            "messages", errors
        );

        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }
}
