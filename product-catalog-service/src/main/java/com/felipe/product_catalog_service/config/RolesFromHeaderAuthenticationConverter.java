package com.felipe.product_catalog_service.config;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

@Component
public class RolesFromHeaderAuthenticationConverter implements ServerAuthenticationConverter {

    private static final String ROLES_HEADER = "X-User-Roles";

    @Override
    public Mono<Authentication> convert(ServerWebExchange exchange) {
        List<String> roles = exchange.getRequest().getHeaders().get(ROLES_HEADER);

        if (roles == null || roles.isEmpty()) {
            return Mono.empty();
        }

        List<SimpleGrantedAuthority> authorities = Stream.of(roles.get(0).split(","))
                .map(String::trim)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        return Mono.just(new UsernamePasswordAuthenticationToken("user", null, authorities));
    }

}
