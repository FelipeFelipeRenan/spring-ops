package com.felipe.product_catalog_service;

import java.util.function.Supplier;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.autoconfigure.RefreshAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;

import com.felipe.product_catalog_service.dto.ProductEvent;
import com.felipe.product_catalog_service.service.ProductService;

import reactor.core.publisher.Flux;

import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
@EnableAutoConfiguration(exclude = {RefreshAutoConfiguration.class})
public class ProductCatalogServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProductCatalogServiceApplication.class, args);
	}

	@Bean
	public Supplier<Flux<Message<ProductEvent>>> productEvents(ProductService productService){
		return productService::getEventPublisher;
	}

}
