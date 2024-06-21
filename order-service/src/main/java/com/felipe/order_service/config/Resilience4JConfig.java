package com.felipe.order_service.config;

import java.time.Duration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.retry.RetryConfig;

@Configuration
public class Resilience4JConfig {
    
    @Bean
    @Primary
    public CircuitBreakerConfig circuitBreakerConfig(){
        return CircuitBreakerConfig.custom()
            .slidingWindowSize(5)
            .failureRateThreshold(50)
            .waitDurationInOpenState(Duration.ofSeconds(5))
            .build();
    }

    @Bean
    public RetryConfig retryConfig(){
        return RetryConfig.custom()
            .maxAttempts(3)
            .waitDuration(Duration.ofSeconds(2))
            .build();
    }



}
