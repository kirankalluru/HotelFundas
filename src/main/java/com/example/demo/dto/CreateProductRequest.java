package com.example.demo.dto;

import com.example.demo.entity.product.ProductCategory;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CreateProductRequest {
    private String name;
    private String description;
    private ProductCategory category;
    private Long branchId;
    private String code;
    private BigDecimal price;

}       