package com.felipe.product_catalog_service.service;

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

    public Mono<Product> findById(Long id){
        return productRepository.findById(id);
    }


    
}
