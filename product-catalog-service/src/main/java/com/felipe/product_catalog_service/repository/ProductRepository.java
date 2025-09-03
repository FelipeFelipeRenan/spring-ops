// src/main/java/com/felipe/product_catalog_service/repository/ProductRepository.java
package com.felipe.product_catalog_service.repository;

import org.springframework.data.r2dbc.repository.R2dbcRepository;

import com.felipe.product_catalog_service.model.Product;

import org.springframework.stereotype.Repository;

import reactor.core.publisher.Mono;

@Repository
public interface ProductRepository extends R2dbcRepository<Product, Long> {
    
    Mono<Product> findBySku(String sku);
}