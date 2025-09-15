// src/main/java/com/felipe/product_catalog_service/config/SecurityConfig.java
package com.felipe.product_catalog_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;
import reactor.core.publisher.Mono;

@Configuration
@EnableReactiveMethodSecurity
public class SecurityConfig {

    private final RolesFromHeaderAuthenticationConverter authenticationConverter;

    public SecurityConfig(RolesFromHeaderAuthenticationConverter authenticationConverter) {
        this.authenticationConverter = authenticationConverter;
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
       
        ReactiveAuthenticationManager trustingManager = authentication -> Mono.just(authentication);

        AuthenticationWebFilter authenticationWebFilter = new AuthenticationWebFilter(trustingManager);
        authenticationWebFilter.setServerAuthenticationConverter(this.authenticationConverter);

        http
            .csrf(ServerHttpSecurity.CsrfSpec::disable)
            .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
            .addFilterAt(authenticationWebFilter, SecurityWebFiltersOrder.AUTHENTICATION)
            .authorizeExchange(exchange -> exchange
                .anyExchange().authenticated()
            );

        return http.build();
    }
}