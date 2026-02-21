package com.example.demo.dto.invoice;

import com.example.demo.entity.invoice.InvoiceStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateInvoiceStatusRequest {
    private InvoiceStatus status;
}