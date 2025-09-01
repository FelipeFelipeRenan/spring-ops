package com.felipe.product_catalog_service.service;

import java.time.Instant;

import org.springframework.stereotype.Service;

import com.felipe.product_catalog_service.model.Product;
import com.felipe.product_catalog_service.repository.ProductRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Flux<Product> findAll() {
        return productRepository.findAll();
    }

    public Mono<Product> findById(Long id) {
        return productRepository.findById(id);
    }

    public Mono<Product> create(Product product) {
        product.setCreatedAt(Instant.now());
        product.setUpdatedAt(Instant.now());

        return productRepository.save(product);
    }

    public Mono<Product> update(Long id, Product product) {
        return productRepository.findById(id)
                .flatMap(existingProduct -> {
                    existingProduct.setName(product.getName());
                    existingProduct.setDescription(product.getDescription());
                    existingProduct.setPrice(product.getPrice());
                    existingProduct.setUpdatedAt(Instant.now());
                    return productRepository.save(existingProduct);
                });
    }

    public Mono<Void> deleteById(Long id) {
        return productRepository.findById(id)
                .flatMap(productRepository::delete);
    }

}
