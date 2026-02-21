package com.example.demo.controller;

import com.example.demo.dto.invoice.InvoiceResponse;
import com.example.demo.dto.invoice.MonthlyInvoiceRequest;
import com.example.demo.dto.invoice.UpdateInvoiceStatusRequest;
import com.example.demo.entity.invoice.Invoice;
import com.example.demo.entity.invoice.InvoiceType;
import com.example.demo.service.InvoiceService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;


@RestController
@RequestMapping("/api/invoices")
public class InvoiceController {

    private final InvoiceService invoiceService;

    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @PostMapping("/generate_monthly")
    public ResponseEntity<InvoiceResponse> generateMonthlyInvoice(
            @RequestBody MonthlyInvoiceRequest request) {

        return ResponseEntity.ok(
                invoiceService.generateMonthlyInvoice(request)
        );
    }

    //Get invoice by ID
    @GetMapping("/{id}")
    public ResponseEntity<InvoiceResponse> getInvoiceById(@PathVariable Long id) {
        return ResponseEntity.ok(invoiceService.getInvoiceById(id));
    }

    //List invoices with pagination
    @GetMapping
    public ResponseEntity<Page<InvoiceResponse>> listInvoices(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) InvoiceType invoiceType) {

        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(invoiceService.listInvoices(pageable,invoiceType));
    }

    // Update invoice status
    @PutMapping("/{id}/status")
    public ResponseEntity<InvoiceResponse> updateInvoiceStatus(
            @PathVariable Long id,
            @RequestBody UpdateInvoiceStatusRequest request) {

        return ResponseEntity.ok(
                invoiceService.updateInvoiceStatus(id, request.getStatus())
        );
    }

    //Find invoices by date range
    @GetMapping("/date-range")
    public ResponseEntity<List<InvoiceResponse>> findByDateRange(
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate startDate,

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate endDate) {

        return ResponseEntity.ok(
                invoiceService.findByDateRange(startDate, endDate)
        );
    }
}