package com.example.demo.service;

import com.example.demo.dto.CreateProductRequest;
import com.example.demo.entity.product.Product;
import com.example.demo.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {
    
    private final ProductRepository productRepository;
    
    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
    
    @Transactional
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
    
    @Transactional
    public Product getProductById(Long id) {
        return productRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Product not found"));
    }
    
    @Transactional
    public Product createProduct(CreateProductRequest request) {
        Product product = new Product();
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setCategory(request.getCategory());
        product.setCode(request.getCode());
        product.setPrice(request.getPrice());
        return productRepository.save(product);
    }
    
    @Transactional
    public Product updateProduct(Long id, CreateProductRequest request) {
        Product product = productRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Product not found"));
            
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setCategory(request.getCategory());
        product.setCode(request.getCode());
        product.setPrice(request.getPrice());
        return productRepository.save(product);
    }
    
    @Transactional
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("Product not found");
        }
        productRepository.deleteById(id);
    }
    
//    @Transactional
//    @Cacheable
//    public List<Product> searchProducts(String name) {
//        Log.info();
//        return new ArrayList<>();
//       // return productRepository.findByNameContainingIgnoreCase(name);
//    }


   
} 