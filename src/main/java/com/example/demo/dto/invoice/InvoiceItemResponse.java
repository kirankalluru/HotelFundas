package com.example.demo.dto.invoice;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class InvoiceItemResponse {

    private String productName;
    private String productCode;
    private BigDecimal price;
    private Integer quantity;
    private BigDecimal lineTotal;
}