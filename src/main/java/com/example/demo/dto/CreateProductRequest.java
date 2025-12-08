package com.example.demo.dto;

import com.example.demo.entity.product.ProductCategory;

public class CreateProductRequest {
    private String name;
    private String description;
    private ProductCategory category;
    private Long branchId;
    private String code;

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ProductCategory getCategory() {
        return category;
    }

    public void setCategory(ProductCategory category) {
        this.category = category;
    }

    public Long getBranchId() {
        return branchId;
    }

    public void setBranchId(Long branchId) {
        this.branchId = branchId;   
    }   

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}       