package com.example.demo.dto.customer;

import com.example.demo.entity.customer.CustomerType;
import com.example.demo.entity.customer.CustomerStatus;
import com.example.demo.entity.common.Address;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotBlank;

public class CreateCustomerRequest {

    @NotBlank(message = "Name is required")
    private String name;
    private String code;
    private String email;

    @NotBlank(message = "Phone is required")
    private String phone;
    private Long branchId;
    
    // Addresses
    private Address shippingAddress;
    private Address billingAddress;
    
    // Tax Information
    private String gstin;
    private String pan;
    
    // Contact Information
    private List<ContactPersonDTO> contactPersons = new ArrayList<>();
    
    // Business Information
    private CustomerType type;
    private CustomerStatus status = CustomerStatus.ACTIVE;

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Address getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(Address shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public Address getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(Address billingAddress) {
        this.billingAddress = billingAddress;
    }

    public String getGstin() {
        return gstin;
    }

    public void setGstin(String gstin) {
        this.gstin = gstin;
    }

    public String getPan() {
        return pan;
    }

    public void setPan(String pan) {
        this.pan = pan;
    }

    public List<ContactPersonDTO> getContactPersons() {
        return contactPersons;
    }

    public void setContactPersons(List<ContactPersonDTO> contactPersons) {
        this.contactPersons = contactPersons;
    }

    public CustomerType getType() {
        return type;
    }

    public void setType(CustomerType type) {
        this.type = type;
    }

    public CustomerStatus getStatus() {
        return status;
    }

    public void setStatus(CustomerStatus status) {
        this.status = status;
    }

    public Long getBranchId() {
        return branchId;
    }

    public void setBranchId(Long branchId) {
        this.branchId = branchId;
    }   
} 