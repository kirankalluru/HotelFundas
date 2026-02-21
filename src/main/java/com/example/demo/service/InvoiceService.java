package com.example.demo.service;

import com.example.demo.dto.invoice.MonthlyInvoiceRequest;
import com.example.demo.entity.customer.Customer;
import com.example.demo.entity.invoice.*;
import com.example.demo.dto.invoice.InvoiceResponse;
import com.example.demo.dto.invoice.InvoiceItemResponse;
import com.example.demo.entity.order.Order;
import com.example.demo.entity.order.OrderItem;
import com.example.demo.entity.order.OrderStatus;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.repository.InvoiceRepository;
import com.example.demo.repository.OrderItemRepository;
import com.example.demo.repository.OrderRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final CustomerRepository customerRepository;
    public InvoiceService(InvoiceRepository invoiceRepository,
                          OrderRepository orderRepository,
                          CustomerRepository customerRepository) {
        this.invoiceRepository = invoiceRepository;
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
    }
    public OrderRepository orderRepository;

    @Transactional
    public Invoice generateInvoice(Order order) {

        // 🔒 Prevent duplicate invoice
        if (invoiceRepository.findByOrderId(order.getId()).isPresent()) {
            throw new RuntimeException("Invoice already exists for this order");
        }

        Invoice invoice = new Invoice();
        invoice.setInvoiceNumber(generateInvoiceNumber());
        invoice.setOrder(order);
        invoice.setInvoiceDate(LocalDate.now());
        invoice.setDueDate(LocalDate.now().plusDays(15));
        invoice.setStatus(InvoiceStatus.GENERATED);
        invoice.setInvoiceType(InvoiceType.ORDER);
        invoice.setCustomer(order.getCustomer());

        BigDecimal subtotal = BigDecimal.ZERO;

        for (OrderItem orderItem : order.getItems()) {

            InvoiceItem item = new InvoiceItem();
            item.setInvoice(invoice);
            item.setProductName(orderItem.getProduct().getName());
            item.setProductCode(orderItem.getProduct().getCode());
            item.setPrice(orderItem.getPriceAtPurchase());
            item.setQuantity(orderItem.getQuantity());

            BigDecimal lineTotal = orderItem.getPriceAtPurchase()
                    .multiply(BigDecimal.valueOf(orderItem.getQuantity()));

            item.setLineTotal(lineTotal);

            subtotal = subtotal.add(lineTotal);

            invoice.getItems().add(item);
        }

        // Simple 18% tax
        BigDecimal tax = subtotal.multiply(BigDecimal.valueOf(0.18));
        BigDecimal total = subtotal.add(tax);

        invoice.setSubtotal(subtotal);
        invoice.setTaxAmount(tax);
        invoice.setTotalAmount(total);

        return invoiceRepository.save(invoice);
    }

    private String generateInvoiceNumber() {
        return "INV-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    @Transactional(readOnly = true)
    public InvoiceResponse getInvoiceById(Long id) {

        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Invoice not found"));

        return mapToResponse(invoice);
    }

    public Page<InvoiceResponse> listInvoices(Pageable pageable,
                                              InvoiceType invoiceType) {

        Page<Invoice> page;

        if (invoiceType != null) {
            page = invoiceRepository.findByInvoiceType(invoiceType, pageable);
        } else {
            page = invoiceRepository.findAll(pageable);
        }

        return page.map(this::mapToResponse);
    }

    @Transactional
    public InvoiceResponse updateInvoiceStatus(Long id, InvoiceStatus newStatus) {

        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Invoice not found"));

        InvoiceStatus currentStatus = invoice.getStatus();

        // If already PAID or CANCELLED → block all changes
        if (currentStatus == InvoiceStatus.PAID) {
            throw new RuntimeException("Paid invoice cannot be modified");
        }

        if (currentStatus == InvoiceStatus.CANCELLED) {
            throw new RuntimeException("Cancelled invoice cannot be modified");
        }

        // Only GENERATED invoices can change
        if (currentStatus == InvoiceStatus.GENERATED) {

            if (newStatus == InvoiceStatus.PAID ||
                    newStatus == InvoiceStatus.CANCELLED) {

                invoice.setStatus(newStatus);

            } else {
                throw new RuntimeException("Invalid status transition");
            }
        }

        return mapToResponse(invoice);
    }

    @Transactional(readOnly = true)
    public List<InvoiceResponse> findByDateRange(LocalDate start, LocalDate end) {

        return invoiceRepository
                .findByInvoiceDateBetween(start, end)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }


    private InvoiceResponse mapToResponse(Invoice invoice) {

        InvoiceResponse response = new InvoiceResponse();

        response.setId(invoice.getId());
        response.setInvoiceNumber(invoice.getInvoiceNumber());
        response.setInvoiceDate(invoice.getInvoiceDate());
        response.setSubtotal(invoice.getSubtotal());
        response.setTaxAmount(invoice.getTaxAmount());
        response.setTotalAmount(invoice.getTotalAmount());
        response.setStatus(invoice.getStatus());
        response.setInvoiceType(invoice.getInvoiceType());

        // Get customer name safely
        if (invoice.getInvoiceType() == InvoiceType.ORDER) {
            response.setCustomerName(
                    invoice.getOrder().getCustomer().getName()
            );
        } else {
            response.setCustomerName(
                    invoice.getCustomer().getName()
            );
        }

        List<InvoiceItemResponse> itemResponses = invoice.getItems()
                .stream()
                .map(item -> {
                    InvoiceItemResponse itemRes = new InvoiceItemResponse();
                    itemRes.setProductName(item.getProductName());
                    itemRes.setProductCode(item.getProductCode());
                    itemRes.setPrice(item.getPrice());
                    itemRes.setQuantity(item.getQuantity());
                    itemRes.setLineTotal(item.getLineTotal());
                    return itemRes;
                })
                .toList();

        response.setItems(itemResponses);

        return response;
    }


    @Transactional
    public InvoiceResponse generateMonthlyInvoice(MonthlyInvoiceRequest request) {

        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        Optional<Invoice> existing = invoiceRepository
                .findByCustomerIdAndBillingMonthAndBillingYearAndInvoiceType(
                        request.getCustomerId(),
                        request.getMonth(),
                        request.getYear(),
                        InvoiceType.MONTHLY
                );

        if (existing.isPresent()) {
            throw new RuntimeException(
                    "Monthly invoice already exists for customer "
                            + request.getCustomerId()
                            + " for "
                            + request.getMonth() + "/" + request.getYear()
            );
        }

        LocalDate startDate = LocalDate.of(request.getYear(), request.getMonth(), 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());

        LocalDateTime start = startDate.atStartOfDay();
        LocalDateTime end = endDate.atTime(23, 59, 59);

        List<Order> orders = orderRepository
                .findByCustomerIdAndStatusAndCreatedAtBetween(
                        request.getCustomerId(),
                        OrderStatus.CONFIRMED,
                        start,
                        end
                );

        if (orders.isEmpty()) {
            throw new RuntimeException("No confirmed orders found for this month");
        }

        Invoice invoice = new Invoice();
        invoice.setInvoiceNumber("INV-" + request.getYear() + "-" + request.getMonth() + "-" + System.currentTimeMillis());
        invoice.setCustomer(customer);
        invoice.setInvoiceDate(LocalDate.now());
        invoice.setDueDate(LocalDate.now().plusDays(15));
        invoice.setStatus(InvoiceStatus.GENERATED);
        invoice.setBillingMonth(request.getMonth());
        invoice.setBillingYear(request.getYear());
        invoice.setNotes(request.getNotes());
        invoice.setInvoiceType(InvoiceType.MONTHLY);

        BigDecimal subtotal = BigDecimal.ZERO;

        for (Order order : orders) {

            for (OrderItem orderItem : order.getItems()) {

                InvoiceItem item = new InvoiceItem();

                item.setInvoice(invoice);
                item.setProductName(orderItem.getProduct().getName());
                item.setProductCode(orderItem.getProduct().getCode());
                item.setPrice(orderItem.getPriceAtPurchase());
                item.setQuantity(orderItem.getQuantity());

                BigDecimal lineTotal = orderItem.getPriceAtPurchase()
                        .multiply(BigDecimal.valueOf(orderItem.getQuantity()));

                item.setLineTotal(lineTotal);

                subtotal = subtotal.add(lineTotal);

                invoice.getItems().add(item);
            }
        }

        BigDecimal tax = subtotal.multiply(BigDecimal.valueOf(0.18));
        BigDecimal total = subtotal.add(tax);

        invoice.setSubtotal(subtotal);
        invoice.setTaxAmount(tax);
        invoice.setTotalAmount(total);

        invoiceRepository.save(invoice);

        return mapToResponse(invoice);
    }
}