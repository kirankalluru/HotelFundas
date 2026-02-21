package com.example.demo.dto.invoice;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MonthlyInvoiceRequest {

    private Long customerId;
    private int month;
    private int year;
    private String notes;
}