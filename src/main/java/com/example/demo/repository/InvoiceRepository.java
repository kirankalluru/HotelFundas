package com.example.demo.repository;

import com.example.demo.entity.invoice.Invoice;
import com.example.demo.entity.invoice.InvoiceType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

    Optional<Invoice> findByOrderId(Long orderId);
    List<Invoice> findByInvoiceDateBetween(LocalDate start, LocalDate end);
    Optional<Invoice>
    findByCustomerIdAndBillingMonthAndBillingYearAndInvoiceType(
            Long customerId,
            Integer billingMonth,
            Integer billingYear,
            InvoiceType invoiceType
    );

    Page<Invoice> findByInvoiceType(InvoiceType invoiceType, Pageable pageable);
}