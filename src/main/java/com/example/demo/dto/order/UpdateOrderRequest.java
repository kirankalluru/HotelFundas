package com.example.demo.dto.order;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateOrderRequest {
    private List<OrderItemRequest> items;
}
