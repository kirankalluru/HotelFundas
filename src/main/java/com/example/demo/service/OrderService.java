package com.example.demo.service;

import com.example.demo.dto.order.OrderItemRequest;
import com.example.demo.dto.order.OrderRequest;
import com.example.demo.entity.customer.Customer;
import com.example.demo.entity.order.Order;
import com.example.demo.entity.order.OrderItem;
import com.example.demo.entity.order.OrderStatus;
import com.example.demo.entity.product.Product;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.dto.order.UpdateOrderRequest;

import javax.transaction.Transactional;
import java.math.BigDecimal;

@RequiredArgsConstructor
@Service
@Transactional
public class OrderService {
    @Autowired
    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final ReferenceNumberGenerator referenceNumberGenerator;
    private final InvoiceService invoiceService;

    public Order placeOrder(OrderRequest request) {

        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        Order order = new Order();
        order.setCustomer(customer);
        order.setStatus(OrderStatus.CREATED);
        order.setOrderNumber(referenceNumberGenerator.generate());

        BigDecimal total = BigDecimal.ZERO;

        for (OrderItemRequest itemRequest : request.getItems()) {

            Product product = productRepository.findById(itemRequest.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(itemRequest.getQuantity());
            orderItem.setPriceAtPurchase(product.getPrice());

            total = total.add(
                    product.getPrice().multiply(
                            BigDecimal.valueOf(itemRequest.getQuantity())
                    )
            );

            order.getItems().add(orderItem);
        }

        order.setTotalAmount(total);

        return orderRepository.save(order);
    }

    @Transactional
    public Order updateOrder(Long orderId, UpdateOrderRequest request) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (order.getStatus() != OrderStatus.CREATED) {
            throw new RuntimeException("Only CREATED orders can be updated");
        }

        // Clear old items
        order.getItems().clear();

        BigDecimal total = BigDecimal.ZERO;

        for (OrderItemRequest itemRequest : request.getItems()) {

            Product product = productRepository.findById(itemRequest.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(itemRequest.getQuantity());
            orderItem.setPriceAtPurchase(product.getPrice());

            total = total.add(
                    product.getPrice().multiply(
                            BigDecimal.valueOf(itemRequest.getQuantity())
                    )
            );

            order.getItems().add(orderItem);
        }

        order.setTotalAmount(total);

        return orderRepository.save(order);
    }


    @Transactional
    public void confirmOrder(Long orderId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (order.getStatus() != OrderStatus.CREATED) {
            throw new RuntimeException("Only CREATED orders can be confirmed");
        }

        order.setStatus(OrderStatus.CONFIRMED);

        orderRepository.save(order);

        invoiceService.generateInvoice(order);
    }

}

