package com.felipe.product_catalog_service.service;

import java.time.Instant;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.felipe.product_catalog_service.dto.CreateProductRequest;
import com.felipe.product_catalog_service.dto.UpdateProductRequest;
import com.felipe.product_catalog_service.exceptions.ProductNotFoundException;
import com.felipe.product_catalog_service.mapper.ProductMapper;
import com.felipe.product_catalog_service.model.Product;
import com.felipe.product_catalog_service.repository.ProductRepository;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.context.annotation.Lazy;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ProductService {

    public static final String PRODUCT_CACHE_KEY = "'product::' + #id";
    public static final String PRODUCT_CACHE_NAME = "products";

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    private final MeterRegistry meterRegistry;
    private final ProductService self;

    private final Counter productsFound;
    private final Counter productsNotFound;

    public ProductService(ProductRepository productRepository, ProductMapper productMapper,
            MeterRegistry meterRegistry, @Lazy ProductService self) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
        this.meterRegistry = meterRegistry;
        this.self = self; // Armazenamos a referência injetada

        this.productsFound = Counter.builder("products.found")
                .description("Número de vezes que um produto foi encontrado pelo ID")
                .register(meterRegistry);

        this.productsNotFound = Counter.builder("products.notfound")
                .description("Número de vezes que um produto não foi encontrado pelo ID")
                .register(meterRegistry);
    }

    @Cacheable(value = PRODUCT_CACHE_NAME, keyGenerator = "cacheKeyGenerator")
    public Flux<Product> findAll(Pageable pageable) {
        return productRepository.findAll(pageable.getSort())
                .skip(pageable.getOffset())
                .take(pageable.getPageSize());
    }

    //@Cacheable(value = PRODUCT_CACHE_NAME, key = PRODUCT_CACHE_KEY)
    public Mono<Product> findById(Long id) {
        return self.findProductCacheable(id)
                .doOnSuccess(product -> {
                    if (product != null) {
                        productsFound.increment();
                    }
                })
                .switchIfEmpty(Mono.defer(() -> {
                    productsNotFound.increment();
                    return Mono.error(new ProductNotFoundException("Product not found"));
                }));
    }

    @Cacheable(value = PRODUCT_CACHE_NAME, key = PRODUCT_CACHE_KEY)
    public Mono<Product> findProductCacheable(Long id) {
        return productRepository.findById(id)
                .switchIfEmpty(Mono.error(new ProductNotFoundException("Product not found")));
    }

    @CacheEvict(value = PRODUCT_CACHE_NAME, allEntries = true)
    public Mono<Product> create(CreateProductRequest request) {
        Product product = productMapper.toEntity(request);
        product.setCreatedAt(Instant.now());
        product.setUpdatedAt(Instant.now());

        return productRepository.save(product)
                .doOnSuccess(savedProduct -> meterRegistry.counter("products.created").increment());
    }

    @Caching(evict = { @CacheEvict(value = PRODUCT_CACHE_NAME, allEntries = true) }, put = {
            @CachePut(value = PRODUCT_CACHE_NAME, key = PRODUCT_CACHE_KEY) })
    public Mono<Product> update(Long id, UpdateProductRequest product) {
        return findById(id)
                .flatMap(existingProduct -> {
                    productMapper.updateEntityFromRequest(product, existingProduct);
                    existingProduct.setUpdatedAt(Instant.now());
                    return productRepository.save(existingProduct);
                });
    }

    @Caching(evict = {
            @CacheEvict(value = PRODUCT_CACHE_NAME, allEntries = true),
            @CacheEvict(value = PRODUCT_CACHE_NAME, key = PRODUCT_CACHE_KEY)
    })
    public Mono<Void> deleteById(Long id) {
        return findById(id)
                .flatMap(productRepository::delete);
    }

}
