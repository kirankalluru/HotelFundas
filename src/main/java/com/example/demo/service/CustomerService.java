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
import com.example.demo.dto.customer.CreateCustomerUserRequest;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.ArrayList;
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
        this.customerRepository = customerRepository;
        this.customerUserRepository = customerUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // ================= CREATE =================

    public CustomerResponse saveCustomer(Long id, CreateCustomerRequest request) {

        Customer customer;

        if (id != null) {
            customer = customerRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Customer not found"));
        } else {
            customer = new Customer();
        }

        setCustomerFields(customer, request);

        return convertToResponse(customerRepository.save(customer));
    }

    // ================= GET =================

    public CustomerResponse getCustomerById(Long id) {
        return customerRepository.findById(id)
                .map(this::convertToResponse)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found"));
    }

    // ================= SEARCH =================

    public Page<CustomerResponse> searchCustomers(String name, String code, Pageable pageable) {

        Page<Customer> customers;

        if ((name != null && !name.isEmpty()) || (code != null && !code.isEmpty())) {
            customers = customerRepository.findByNameContainingIgnoreCase(
                    name != null ? name : "", pageable
            );
        } else {
            customers = customerRepository.findAll(pageable);
        }

        return customers.map(this::convertToResponse);
    }

    // REQUIRED BY CONTROLLER
    public List<CustomerDTO> searchCustomersByName(String name) {
        return customerRepository.findByNameContainingIgnoreCase(name)
                .stream()
                .map(c -> new CustomerDTO(c.getId(), c.getName()))
                .collect(Collectors.toList());
    }

    // ================= DELETE =================

    public void deleteCustomer(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        customerRepository.delete(customer);
    }

    // ================= FIELD MAPPING =================

    private void setCustomerFields(Customer customer, CreateCustomerRequest request) {

        customer.setName(request.getName());
        customer.setCode(request.getCode());
        customer.setEmail(request.getEmail());
        customer.setPhone(request.getPhone());

        if (request.getBranchId() == null) {
            throw new ServiceException("branchId is required");
        }
        customer.setBranchId(request.getBranchId());

        customer.setDrivingLicense(request.getDrivingLicense());

        customer.setShippingAddress(request.getShippingAddress());
        customer.setBillingAddress(request.getBillingAddress());

        customer.setGstin(request.getGstin());
        customer.setPan(request.getPan());

        customer.setType(request.getType());
        customer.setStatus(request.getStatus());

        if (request.getContactPersons() != null) {
            customer.getContactPersons().clear();
            request.getContactPersons().forEach(dto -> {
                ContactPerson cp = new ContactPerson();
                cp.setName(dto.getName());
                cp.setEmail(dto.getEmail());
                cp.setPhone(dto.getPhone());
                cp.setTag(dto.getTag());
                cp.setCustomer(customer);
                customer.getContactPersons().add(cp);
            });
        }
    }

    // ================= UPDATE =================

    public CustomerResponse updateCustomer(Long customerId, UpdateCustomerRequest request) {

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        if (request.getName() != null) customer.setName(request.getName());
        if (request.getEmail() != null) customer.setEmail(request.getEmail());
        if (request.getPhone() != null) customer.setPhone(request.getPhone());
        if (request.getGstin() != null) customer.setGstin(request.getGstin());
        if (request.getPan() != null) customer.setPan(request.getPan());
        if (request.getDrivingLicense() != null) {
            customer.setDrivingLicense(request.getDrivingLicense());
        }

        if (request.getType() != null) {
            customer.setType(CustomerType.valueOf(request.getType()));
        }
        if (request.getStatus() != null) {
            customer.setStatus(CustomerStatus.valueOf(request.getStatus()));
        }

        if (request.getBillingAddress() != null) {
            customer.setBillingAddress(request.getBillingAddress());
        }
        if (request.getShippingAddress() != null) {
            customer.setShippingAddress(request.getShippingAddress());
        }

        if (request.getContactPersons() != null) {
            customer.getContactPersons().clear();
            request.getContactPersons().forEach(dto -> {
                ContactPerson cp = new ContactPerson();
                cp.setName(dto.getName());
                cp.setEmail(dto.getEmail());
                cp.setPhone(dto.getPhone());
                cp.setTag(dto.getTag());
                cp.setCustomer(customer);
                customer.getContactPersons().add(cp);
            });
        }

        return convertToResponse(customerRepository.save(customer));
    }

    // ================= RESPONSE =================

    private CustomerResponse convertToResponse(Customer customer) {

        CustomerResponse response = new CustomerResponse();
        response.setId(customer.getId());
        response.setName(customer.getName());
        response.setCode(customer.getCode());
        response.setEmail(customer.getEmail());
        response.setPhone(customer.getPhone());
        response.setGstin(customer.getGstin());
        response.setPan(customer.getPan());
        response.setDrivingLicense(customer.getDrivingLicense());
        response.setStatus(customer.getStatus().toString());
        response.setType(customer.getType().toString());
        response.setBillingAddress(convertToAddressDTO(customer.getBillingAddress()));
        response.setShippingAddress(convertToAddressDTO(customer.getShippingAddress()));
        response.setContactPersons(
                customer.getContactPersons().stream()
                        .map(this::convertToDTO)
                        .collect(Collectors.toList())
        );
        response.setCreatedAt(customer.getCreatedAt());
        response.setUpdatedAt(customer.getUpdatedAt());

        return response;
    }

    private AddressDTO convertToAddressDTO(Address address) {
        if (address == null) return null;

        AddressDTO dto = new AddressDTO();
        dto.setAddressLine1(address.getAddressLine1());
        dto.setAddressLine2(address.getAddressLine2());
        dto.setCity(address.getCity());
        dto.setState(address.getState());
        dto.setCountry(address.getCountry());
        dto.setPincode(address.getPincode());
        return dto;
    }

    private ContactPersonDTO convertToDTO(ContactPerson cp) {
        ContactPersonDTO dto = new ContactPersonDTO();
        dto.setId(cp.getId());
        dto.setName(cp.getName());
        dto.setEmail(cp.getEmail());
        dto.setPhone(cp.getPhone());
        dto.setTag(cp.getTag());
        return dto;
    }

    // ================= BULK =================

    public List<CustomerResponse> createCustomersInBulk(List<CreateCustomerRequest> requests) {
        List<CustomerResponse> responses = new ArrayList<>();
        for (CreateCustomerRequest request : requests) {
            responses.add(saveCustomer(null, request));
        }
        return responses;
    }

    // ================= CUSTOMER USER =================

    public CustomerUserResponse createCustomerUser(CreateCustomerUserRequest request) {

        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() ->
                        new EntityNotFoundException("Customer not found with id: " + request.getCustomerId()));

        CustomerUser user = new CustomerUser();
        user.setEmail(request.getEmail());
        user.setName(request.getName());
        user.setPhone(request.getPhone());
        user.setRole(request.getRole());
        user.setCustomer(customer);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        customerUserRepository.save(user);

        CustomerUserResponse response = new CustomerUserResponse();
        response.setId(user.getId());
        response.setEmail(user.getEmail());
        response.setCustomerId(customer.getId());
        response.setName(user.getName());
        response.setPhone(user.getPhone());
        response.setRole(user.getRole());

        return response;
    }
}
