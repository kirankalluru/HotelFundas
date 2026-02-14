package com.example.demo.dto.order;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderRequest {

    private Long customerId;

    private List<OrderItemRequest> items;
}
