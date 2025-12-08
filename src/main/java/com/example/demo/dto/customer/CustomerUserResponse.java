package com.example.demo.dto.customer;
import com.example.demo.entity.customer.CustomerUserRole;

public class CustomerUserResponse {
    private Long id;
    private String email;
    private String name;
    private String phone;
    private CustomerUserRole role;

    private Long customerId;

    // Getters and setters

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;   
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {    
        this.email = email;
    }   
    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public CustomerUserRole getRole(){
        return this.role;
    }

    public void setRole(CustomerUserRole role){
        this.role = role;
    }   

    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone; 
    }

    public Long getCustomerId() {
        return customerId;
    }
    public void setCustomerId(Long customerId) {
        this.customerId = customerId;   
    }
}