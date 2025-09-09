package com.felipe.product_catalog_service.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.felipe.product_catalog_service.dto.CreateProductRequest;
import com.felipe.product_catalog_service.dto.ProductResponse;
import com.felipe.product_catalog_service.dto.UpdateProductRequest;
import com.felipe.product_catalog_service.mapper.ProductMapper;
import com.felipe.product_catalog_service.service.ProductService;

import io.micrometer.core.instrument.MeterRegistry;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/v1/api/products")
@Tag(name = "Product Catalog API", description = "Endpoints para gerir o catálogo de produtos")
public class ProductController {

    private final ProductService productService;
    private final ProductMapper productMapper;
    private final MeterRegistry meterRegistry;

    public ProductController(ProductService productService, ProductMapper productMapper, MeterRegistry meterRegistry) {
        this.productService = productService;
        this.productMapper = productMapper;
        this.meterRegistry = meterRegistry;
    }

    @GetMapping
    @Operation(summary = "Lista todos produtos de forma paginada")
    public Flux<ProductResponse> getAllProducts(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "sort", defaultValue = "id,asc") String[] sort) {

        // Lógica para converter o array de 'sort' em um objeto Sort do Spring
        List<Sort.Order> orders = new ArrayList<>();
        if (sort[0].contains(",")) {
            // Múltiplos parâmetros de sort
            for (String sortOrder : sort) {
                String[] _sort = sortOrder.split(",");
                orders.add(new Sort.Order(_sort[1].equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC,
                        _sort[0]));
            }
        } else {
            // Apenas um parâmetro de sort
            orders.add(new Sort.Order(sort[1].equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC,
                    sort[0]));
        }

        // Construímos o objeto Pageable manualmente
        Pageable pageable = PageRequest.of(page, size, Sort.by(orders));

        return productService.findAll(pageable)
                .map(productMapper::toResponse);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca um produto pelo seu ID")
    @ApiResponse(responseCode = "200", description = "Produto encontrado")
    @ApiResponse(responseCode = "404", description = "Produto não encontrado", content = @Content)
    public Mono<ResponseEntity<ProductResponse>> getProductById(@PathVariable Long id) {
        return productService.findById(id)
                .map(productMapper::toResponse)
                .map(ResponseEntity::ok)
                .doOnSuccess(response -> {
                    if (response != null && response.getStatusCode().is2xxSuccessful()) {
                        meterRegistry.counter("products.found").increment();
                    }
                }).defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Cria um novo produto")
    @ApiResponse(responseCode = "201", description = "Produto criado com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados de entrada inválido", content = @Content)
    public Mono<ProductResponse> createProduct(@Valid @RequestBody Mono<CreateProductRequest> request) {
        return request
                .flatMap(productService::create)
                .map(productMapper::toResponse);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Autaliza um produto existente")
    @ApiResponse(responseCode = "200", description = "Produto autalizado com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados de entrada inválido", content = @Content)
    public Mono<ProductResponse> updateProduct(@PathVariable Long id, @RequestBody UpdateProductRequest product) {

        return productService.update(id, product)
                .map(productMapper::toResponse);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deleta um produto")
    @ApiResponse(responseCode = "201", description = "Produto deletador com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados de entrada inválido", content = @Content)
    public Mono<Void> deleteProduct(@PathVariable Long id) {
        return productService.deleteById(id);
    }

}
