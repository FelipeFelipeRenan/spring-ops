// src/main/java/com/felipe/product_catalog_service/exception/ProductNotFoundException.java
package com.felipe.product_catalog_service.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// Esta anotação é um atalho, mas faremos de uma forma mais robusta.
// Por enquanto, ela já ajuda a sinalizar a intenção.
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ProductNotFoundException extends RuntimeException {

    public ProductNotFoundException(String message) {
        super(message);
    }
}