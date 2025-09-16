package com.felipe.product_catalog_service.service;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.felipe.product_catalog_service.model.Product;
import com.felipe.product_catalog_service.repository.ProductRepository;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class RepositoryResilienceDecorator {
    
    private final ProductRepository productRepository;

    public RepositoryResilienceDecorator(ProductRepository  productRespository) {
        this.productRepository = productRespository;
    }

    @CircuitBreaker(name = "postgres", fallbackMethod = "fallbackEmptyFlux")
    @Retry(name = "postgres")
        public Flux<Product> findAll(Pageable pageable) {
        return productRepository.findAll(pageable.getSort())
                .skip(pageable.getOffset())
                .take(pageable.getPageSize());
    }

    @CircuitBreaker(name = "postgres")
    @Retry(name = "postgres")
    public Mono<Product> findById(Long id) {
        return productRepository.findById(id);
    }

    @CircuitBreaker(name = "postgres")
    @Retry(name = "postgres")
    public Mono<Product> save(Product product) {
        return productRepository.save(product);
    }

    @CircuitBreaker(name = "postgres")
    @Retry(name = "postgres")
    public Mono<Void> delete(Product product) {
        return productRepository.delete(product);
    }

    // metodo de fallback para o findAll
    private Flux<Product> fallbackEmptyFlux(Throwable ex){
        System.err.println("Fallback para findAll ativado devido a: " + ex.getMessage());
        return Flux.empty();
    }


    
}
