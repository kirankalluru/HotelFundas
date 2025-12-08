package com.example.demo.controller;

import com.example.demo.dto.customer.CreateCustomerRequest;
import com.example.demo.dto.customer.CustomerResponse;
import com.example.demo.service.CustomerService;
import com.example.demo.entity.customer.CustomerType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import com.example.demo.dto.CustomerDTO;
import com.example.demo.dto.customer.UpdateCustomerRequest;
import org.springframework.http.HttpStatus;
import com.example.demo.entity.customer.Customer;
import com.example.demo.dto.customer.CreateCustomerUserRequest;
import com.example.demo.dto.customer.CustomerUserResponse;

@RestController
@RequestMapping("/api/customers")
@CrossOrigin(origins = "*")
public class CustomerController {
    
    private final CustomerService customerService;
    
    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }
    
    @PostMapping
    public ResponseEntity<CustomerResponse> saveCustomer(
            @RequestParam(required = false) Long id,
            @RequestBody CreateCustomerRequest request) {
        CustomerResponse response = customerService.saveCustomer(id, request);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponse> getCustomer(@PathVariable Long id) {
        return ResponseEntity.ok(customerService.getCustomerById(id));
    }
    
    @GetMapping
    public ResponseEntity<Page<CustomerResponse>> getAllCustomers(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String code,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "100") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection) {
        
        Sort.Direction direction = sortDirection.equalsIgnoreCase("desc") ? 
            Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, 100, Sort.by(direction, sortBy));
        
        return ResponseEntity.ok(customerService.searchCustomers(name, code, pageable));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/types")
    public ResponseEntity<List<String>> getCustomerTypes() {
        List<CustomerType> customerTypes = Arrays.asList(CustomerType.values());
        return ResponseEntity.ok(customerTypes.stream()
            .map(CustomerType::name)
            .collect(Collectors.toList()));
    }

    @GetMapping("/search")
    public ResponseEntity<List<CustomerDTO>> searchCustomersByName(@RequestParam String name) {
        List<CustomerDTO> customers = customerService.searchCustomersByName(name);
        return ResponseEntity.ok(customers);
    }

    @PutMapping("/{customerId}")
    @ResponseStatus(HttpStatus.OK)
    public CustomerResponse updateCustomer(@PathVariable Long customerId, @RequestBody UpdateCustomerRequest request) {
        return customerService.updateCustomer(customerId, request);
    }

    @PostMapping("/bulk")
    public ResponseEntity<List<CustomerResponse>> createCustomersInBulk(@RequestBody List<CreateCustomerRequest> customerRequests) {
        List<CustomerResponse> createdCustomers = customerService.createCustomersInBulk(customerRequests);
        return new ResponseEntity<>(createdCustomers, HttpStatus.CREATED);
    }

    @PostMapping("/{customerId}/users")
    public ResponseEntity<?> createCustomerUser(
            @PathVariable Long customerId,
            @RequestBody CreateCustomerUserRequest request) {
        request.setCustomerId(customerId);
        try {
            CustomerUserResponse response = customerService.createCustomerUser(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to create customer user");
        }
    }
}