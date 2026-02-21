package com.example.demo.entity.invoice;

import com.example.demo.entity.common.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "invoice_items")
public class InvoiceItem extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "invoice_id")
    private Invoice invoice;

    private String productName;
    private String productCode;
    private BigDecimal price;
    private Integer quantity;
    private BigDecimal lineTotal;
}