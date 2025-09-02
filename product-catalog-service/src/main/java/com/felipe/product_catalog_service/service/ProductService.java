package com.felipe.product_catalog_service.service;

import java.time.Instant;

import org.springframework.stereotype.Service;

import com.felipe.product_catalog_service.dto.CreateProductRequest;
import com.felipe.product_catalog_service.exceptions.ProductNotFoundException;
import com.felipe.product_catalog_service.mapper.ProductMapper;
import com.felipe.product_catalog_service.model.Product;
import com.felipe.product_catalog_service.repository.ProductRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public ProductService(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    public Flux<Product> findAll() {
        return productRepository.findAll();
    }

    public Mono<Product> findById(Long id) {
        return productRepository.findById(id)
                .switchIfEmpty(Mono.error(new ProductNotFoundException("Product not found with ID: " + id)));
    }

    public Mono<Product> create(CreateProductRequest request) {
        Product product = productMapper.toEntity(request);
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
