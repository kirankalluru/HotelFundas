package com.example.demo.dto.customer;

import com.example.demo.entity.customer.CustomerType;
import com.example.demo.entity.customer.CustomerStatus;
import com.example.demo.dto.common.AddressDTO;
import com.example.demo.entity.common.Address;
import java.util.List;

public class UpdateCustomerRequest {
    private String name;
    private String email;
    private String phone;
    private String gstin;
    private String pan;
    private String type;
    private String status;
    private Address billingAddress;
    private Address shippingAddress;
    private List<ContactPersonDTO> contactPersons;

    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

  

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Address getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(Address billingAddress) {
        this.billingAddress = billingAddress;
    }

    public Address getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(Address shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public List<ContactPersonDTO> getContactPersons() {
        return contactPersons;
    }

    public void setContactPersons(List<ContactPersonDTO> contactPersons) {
        this.contactPersons = contactPersons;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }   
} 