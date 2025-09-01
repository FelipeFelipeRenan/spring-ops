package com.felipe.product_catalog_service.mapper;

import org.springframework.stereotype.Component;

import com.felipe.product_catalog_service.dto.CreateProductRequest;
import com.felipe.product_catalog_service.dto.ProductResponse;
import com.felipe.product_catalog_service.model.Product;

@Component
public class ProductMapper {

    public Product toEntity(CreateProductRequest request) {
        Product product = new Product();
        product.setSku(request.sku());
        product.setName(request.name());
        product.setDescription(request.description());
        product.setPrice(request.price());
        product.setBrand(request.brand());
        product.setCategory(request.category());
        product.setActive(true); // Valor padrão ao criar
        return product;
    }

    public ProductResponse toResponse(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getSku(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getBrand(),
                product.getCategory(),
                product.isActive(),
                product.getCreatedAt(),
                product.getUpdatedAt(),
                product.getVersion());
    }
}
