package com.felipe.product_catalog_service.service;

import java.time.OffsetDateTime;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.felipe.product_catalog_service.dto.CreateProductRequest;
import com.felipe.product_catalog_service.dto.ProductEvent;
import com.felipe.product_catalog_service.dto.UpdateProductRequest;
import com.felipe.product_catalog_service.exceptions.ProductNotFoundException;
import com.felipe.product_catalog_service.mapper.ProductMapper;
import com.felipe.product_catalog_service.model.Product;
import com.felipe.product_catalog_service.repository.ProductRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

@Service
public class ProductService {

    public static final String PRODUCT_CACHE_KEY = "'product::' + #id";
    public static final String PRODUCT_CACHE_NAME = "products";

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    private final Sinks.Many<Message<ProductEvent>> eventSink = Sinks.many().multicast().onBackpressureBuffer();

    public ProductService(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;

    }

    @Cacheable(value = PRODUCT_CACHE_NAME, keyGenerator = "cacheKeyGenerator")
    public Flux<Product> findAll(Pageable pageable) {
        return productRepository.findAll(pageable.getSort())
                .skip(pageable.getOffset())
                .take(pageable.getPageSize());
    }

    // @Cacheable(value = PRODUCT_CACHE_NAME, key = PRODUCT_CACHE_KEY)
    public Mono<Product> findById(Long id) {
        return this.findProductCacheable(id);
    }

    @Cacheable(value = PRODUCT_CACHE_NAME, key = PRODUCT_CACHE_KEY)
    public Mono<Product> findProductCacheable(Long id) {
        return productRepository.findById(id)
                .switchIfEmpty(Mono.error(new ProductNotFoundException("Product not found")));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @CacheEvict(value = PRODUCT_CACHE_NAME, allEntries = true)
    public Mono<Product> create(CreateProductRequest request) {
        Product product = productMapper.toEntity(request);
        OffsetDateTime now = OffsetDateTime.now();

        product.setCreatedAt(now);
        product.setUpdatedAt(now);

        return productRepository.save(product).doOnSuccess(savedProduct -> {
            ProductEvent event = new ProductEvent("PRODUCT_CREATED", savedProduct.getSku(), savedProduct.getName(),
                    savedProduct.getPrice());
            Message<ProductEvent> message = MessageBuilder.withPayload(event).build();
            eventSink.tryEmitNext(message);
        });
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Caching(evict = { @CacheEvict(value = PRODUCT_CACHE_NAME, allEntries = true) }, put = {
            @CachePut(value = PRODUCT_CACHE_NAME, key = PRODUCT_CACHE_KEY) })
    public Mono<Product> update(Long id, UpdateProductRequest product) {
        return findById(id)
                .flatMap(existingProduct -> {
                    productMapper.updateEntityFromRequest(product, existingProduct);
                    existingProduct.setUpdatedAt(OffsetDateTime.now());
                    return productRepository.save(existingProduct);
                });
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Caching(evict = {
            @CacheEvict(value = PRODUCT_CACHE_NAME, allEntries = true),
            @CacheEvict(value = PRODUCT_CACHE_NAME, key = PRODUCT_CACHE_KEY)
    })
    public Mono<Void> deleteById(Long id) {
        return findById(id)
                .flatMap(productRepository::delete);
    }

    public Flux<Message<ProductEvent>> getEventPublisher() {
        return eventSink.asFlux();
    }

}
