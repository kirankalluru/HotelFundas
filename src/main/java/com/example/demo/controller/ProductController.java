package com.example.demo.controller;

import com.example.demo.dto.CreateProductRequest;
import com.example.demo.entity.product.Product;
import com.example.demo.entity.product.ProductCategory;
import com.example.demo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "*", allowedHeaders = {"Content-Type", "X-Company-ID", "Authorization"})
public class ProductController {
    
    private final ProductService productService;
    
    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }
    
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }
    
    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody CreateProductRequest request) {
        return ResponseEntity.ok(productService.createProduct(request));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(
            @PathVariable Long id,
            @RequestBody CreateProductRequest request) {
        return ResponseEntity.ok(productService.updateProduct(id, request));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok().build();
    }
    
//    @GetMapping("/search")
//    public ResponseEntity<List<Product>> searchProducts(@RequestParam String name) {
//        return ResponseEntity.ok(productService.searchProducts(name));
//    }
    
    @GetMapping("/categories")
    public ResponseEntity<List<String>> getProductCategories() {
        List<String> categories = Arrays.stream(ProductCategory.values())
                                        .map(Enum::name)
                                        .collect(Collectors.toList());
        return ResponseEntity.ok(categories);
    }
} 