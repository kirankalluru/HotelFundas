package com.example.demo.dto.invoice;

import com.example.demo.entity.invoice.InvoiceStatus;
import com.example.demo.entity.invoice.InvoiceType;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class InvoiceResponse {

    private Long id;
    private String invoiceNumber;
    private LocalDate invoiceDate;
    private BigDecimal subtotal;
    private BigDecimal taxAmount;
    private BigDecimal totalAmount;
    private InvoiceStatus status;
    private InvoiceType invoiceType;
    private String customerName;

    private List<InvoiceItemResponse> items;
}