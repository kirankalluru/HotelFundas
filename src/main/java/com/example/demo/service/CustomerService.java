package com.example.demo.service;

import com.example.demo.dto.customer.CreateCustomerRequest;
import com.example.demo.dto.customer.CustomerResponse;
import com.example.demo.dto.customer.UpdateCustomerRequest;
import com.example.demo.dto.customer.ContactPersonDTO;
import com.example.demo.dto.customer.CustomerUserResponse;
import com.example.demo.dto.common.AddressDTO;
import com.example.demo.dto.CustomerDTO;
import com.example.demo.entity.common.Address;
import com.example.demo.entity.customer.*;
import com.example.demo.exception.ServiceException;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.repository.CustomerUserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;
import com.example.demo.dto.customer.CreateCustomerUserRequest;
import java.util.stream.Collectors;


@Service
@Transactional
public class CustomerService {
    
    private final CustomerRepository customerRepository;
    private final CustomerUserRepository customerUserRepository;
        private final PasswordEncoder passwordEncoder;
    
    @Autowired
    public CustomerService(CustomerRepository customerRepository,
    CustomerUserRepository customerUserRepository,
            PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder; 
        this.customerRepository = customerRepository;
        this.customerUserRepository = customerUserRepository;
    }
    
    public CustomerResponse saveCustomer(Long id, CreateCustomerRequest request) {
        Customer customer;
        // if(StringUtils.isEmpty(request.getPhone()) || StringUtils.isEmpty(request.getEmail()) || StringUtils.isEmpty(request.getName())) {
        //     throw new ServiceException("Phone, Email and Name are required");
        // }
        if (id != null) {
            // Update existing customer
            customer = customerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found"));
        } else {
            // Create new customer
            customer = new Customer();



            // if(customerRepository.findByPhone(request.getPhone()).isPresent()) {
            //     throw new ServiceException("Customer already exists with this phone number "+request.getPhone());
            // }

            // if(customerRepository.findByEmail(request.getEmail()).isPresent()) {
            //     throw new ServiceException("Customer already exists with this email "+request.getEmail());
            // }
        }
        
        setCustomerFields(customer, request);
        
        if (id == null) {
            customer.setCreatedAt(LocalDateTime.now());
        }
        customer.setUpdatedAt(LocalDateTime.now());
        
        Customer savedCustomer = customerRepository.save(customer);
        return convertToResponse(savedCustomer);
    }
    
    public CustomerResponse getCustomerById(Long id) {
        return customerRepository.findById(id)
            .map(this::convertToResponse)
            .orElseThrow(() -> new EntityNotFoundException("Customer not found"));
    }
    
    @Transactional
    public Page<CustomerResponse> searchCustomers(String name, String code, Pageable pageable) {
        Page<Customer> customers;
        
        if ((name != null && !name.isEmpty()) || (code != null && !code.isEmpty())) {
            customers = customerRepository.findByNameContainingIgnoreCase(
                name != null ? name : "", 
                pageable
            );
        } else {
            customers = customerRepository.findAll(pageable);
        }
        
        return customers.map(this::convertToResponse);
    }
    
    public void deleteCustomer(Long id) {
        Customer customer = customerRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Customer not found"));
            
        // Check if customer can be deleted (no active agreements, etc)
        customerRepository.delete(customer);
    }
    
    private void setCustomerFields(Customer customer, CreateCustomerRequest request) {
        // Basic Information
        customer.setName(request.getName());
        customer.setCode(request.getCode());
        customer.setEmail(request.getEmail());
        customer.setPhone(request.getPhone());
       
        // Addresses
        customer.setShippingAddress(request.getShippingAddress());
        customer.setBillingAddress(request.getBillingAddress());
        
        // Tax Information
        customer.setGstin(request.getGstin());
        customer.setPan(request.getPan());
        
        // Business Information
        customer.setType(request.getType());
        customer.setStatus(request.getStatus());

        // Handle contact persons
        if (request.getContactPersons() != null) {
            customer.getContactPersons().clear(); // Clear existing contacts
            request.getContactPersons().forEach(dto -> {
                ContactPerson contactPerson = new ContactPerson();
                contactPerson.setName(dto.getName());
                contactPerson.setEmail(dto.getEmail());
                contactPerson.setPhone(dto.getPhone());
                contactPerson.setTag(dto.getTag());
                contactPerson.setCustomer(customer);
                customer.getContactPersons().add(contactPerson);
            });
        }
    }
    
    private void setCustomerFields(Customer customer, UpdateCustomerRequest request) {
        // Basic Information
        if (request.getName() != null) customer.setName(request.getName());
        if (request.getEmail() != null) customer.setEmail(request.getEmail());
        if (request.getPhone() != null) customer.setPhone(request.getPhone());
        
        // Addresses
        if (request.getShippingAddress() != null) customer.setShippingAddress(request.getShippingAddress());
        if (request.getBillingAddress() != null) customer.setBillingAddress(request.getBillingAddress());
        
        // Tax Information
        if (request.getGstin() != null) customer.setGstin(request.getGstin());
        if (request.getPan() != null) customer.setPan(request.getPan());
        
        // Business Information
        if (request.getType() != null) customer.setType(CustomerType.valueOf(request.getType()));
        if (request.getStatus() != null) customer.setStatus(CustomerStatus.valueOf(request.getStatus()));

        // Handle contact persons
        if (request.getContactPersons() != null) {
            // Clear existing contact persons
            customer.getContactPersons().clear();
            
            // Add new contact persons
            request.getContactPersons().forEach(dto -> {
                ContactPerson contactPerson = new ContactPerson();
                contactPerson.setName(dto.getName());
                contactPerson.setEmail(dto.getEmail());
                contactPerson.setPhone(dto.getPhone());
                contactPerson.setTag(dto.getTag());
                contactPerson.setCustomer(customer);
                customer.getContactPersons().add(contactPerson);
            });
        }
    }
    
    private CustomerResponse convertToResponse(Customer customer) {
        CustomerResponse response = new CustomerResponse();
        response.setId(customer.getId());
        response.setName(customer.getName());
        response.setCode(customer.getCode());
        response.setEmail(customer.getEmail());
        response.setPhone(customer.getPhone());
        response.setGstin(customer.getGstin());
        response.setPan(customer.getPan());
        response.setStatus(customer.getStatus().toString());
        response.setType(customer.getType().toString());
        response.setBillingAddress(convertToAddressDTO(customer.getBillingAddress()));
        response.setShippingAddress(convertToAddressDTO(customer.getShippingAddress()));
        
        // Convert contact persons
        response.setContactPersons(customer.getContactPersons().stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList()));
            
        response.setCreatedAt(customer.getCreatedAt());
        response.setUpdatedAt(customer.getUpdatedAt());
        return response;
    }

    private AddressDTO convertToAddressDTO(Address address) {
        if (address == null) {
            return null;
        }
        AddressDTO dto = new AddressDTO();
        dto.setAddressLine1(address.getAddressLine1());
        dto.setAddressLine2(address.getAddressLine2());
        dto.setCity(address.getCity());
        dto.setState(address.getState());
        dto.setCountry(address.getCountry());
        dto.setPincode(address.getPincode());
        return dto;
    }

    private ContactPersonDTO convertToDTO(ContactPerson contactPerson) {
        ContactPersonDTO dto = new ContactPersonDTO();
        dto.setId(contactPerson.getId());
        dto.setName(contactPerson.getName());
        dto.setEmail(contactPerson.getEmail());
        dto.setPhone(contactPerson.getPhone());
        dto.setTag(contactPerson.getTag());
        return dto;
    }

    public List<CustomerDTO> searchCustomersByName(String name) {
        List<Customer> customers = customerRepository.findByNameContainingIgnoreCase(name);
        return customers.stream()
            .map(customer -> new CustomerDTO(customer.getId(), customer.getName()))
            .collect(Collectors.toList());
    }

    @Transactional
    public CustomerResponse updateCustomer(Long customerId, UpdateCustomerRequest request) {
        Customer customer = customerRepository.findById(customerId)
            .orElseThrow(() -> new RuntimeException("Customer not found"));

        customer.setName(request.getName());
        customer.setEmail(request.getEmail());
        customer.setPhone(request.getPhone());
        customer.setGstin(request.getGstin());
        customer.setPan(request.getPan());
        customer.setType(CustomerType.valueOf(request.getType()));
        customer.setStatus(CustomerStatus.valueOf(request.getStatus()));

        // Update addresses
        customer.setBillingAddress(request.getBillingAddress());
        customer.setShippingAddress(request.getShippingAddress());

        // Update contact persons
        customer.getContactPersons().clear();
        request.getContactPersons().forEach(dto -> {
            ContactPerson contactPerson = new ContactPerson();
            contactPerson.setName(dto.getName());
            contactPerson.setEmail(dto.getEmail());
            contactPerson.setPhone(dto.getPhone());
            contactPerson.setTag(dto.getTag());
            contactPerson.setCustomer(customer);
            customer.getContactPersons().add(contactPerson);
        });

        customerRepository.save(customer);

        return convertToResponse(customer);
    }

    public List<CustomerResponse> createCustomersInBulk(List<CreateCustomerRequest> customerRequests) {
        List<CustomerResponse> customerResponses = new ArrayList<>();
        customerRequests.forEach(request -> {
            customerResponses.add(saveCustomer(null, request));
        });
        return customerResponses;
    }

    public CustomerUserResponse createCustomerUser(CreateCustomerUserRequest request) {
        // 1. Fetch Customer by request.getCustomerId()
        Customer customer = customerRepository.findById(request.getCustomerId())
            .orElseThrow(() -> new EntityNotFoundException("Customer not found with id: " + request.getCustomerId()));

        // 2. Create new CustomerUser, set fields, hash password
        CustomerUser customerUser = new CustomerUser();
        customerUser.setEmail(request.getEmail());
        customerUser.setName(request.getName());
        customerUser.setPhone(request.getPhone());
        customerUser.setRole(request.getRole());
        customerUser.setCustomer(customer);

        // Hash password (assuming you have a PasswordEncoder bean)
        
        customerUser.setPassword(passwordEncoder.encode(request.getPassword()));

        // 3. Save CustomerUser using customerUserRepository
        customerUserRepository.save(customerUser);
        // Map to response DTO
        CustomerUserResponse response = new CustomerUserResponse();
        response.setId(customerUser.getId());
        response.setEmail(customerUser.getEmail());
        response.setCustomerId(customer.getId());
        // Set other fields
        response.setName(customerUser.getName());
        response.setPhone(customerUser.getPhone());
        response.setRole(customerUser.getRole());
        return response;
    }
}