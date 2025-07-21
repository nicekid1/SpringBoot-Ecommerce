package com.shop.ecommerce.controller;

import com.shop.ecommerce.dto.ProductResponse;
import com.shop.ecommerce.dto.ProductRequest;
import com.shop.ecommerce.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;
    public ProductController(ProductService productService){
        this.productService = productService;
    }
}
