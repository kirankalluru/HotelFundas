package com.example.demo.repository;

import com.example.demo.entity.order.Order;
import com.example.demo.entity.order.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByCustomerIdAndStatusAndCreatedAtBetween(
            Long customerId,
            OrderStatus status,
            LocalDateTime start,
            LocalDateTime end
    );
}


