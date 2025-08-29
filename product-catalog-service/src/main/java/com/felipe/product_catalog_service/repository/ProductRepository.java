// src/main/java/com/felipe/product_catalog_service/repository/ProductRepository.java
package com.felipe.product_catalog_service.repository;

import com.felipe.product_catalog_service.model.Product;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface ProductRepository extends ReactiveCrudRepository<Product, Long> {
    
    Mono<Product> findBySku(String sku);
}