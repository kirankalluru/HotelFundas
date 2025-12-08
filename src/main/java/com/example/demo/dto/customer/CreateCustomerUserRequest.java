package com.example.demo.dto.customer;
import com.example.demo.entity.customer.CustomerUserRole;

public class CreateCustomerUserRequest {
    private String email;
    private String password;
    private String phone;
    private String name;
    private CustomerUserRole role;
    private Long customerId;

    // Getters and setters

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;     

    }
    public String getPassword() {
        return password;
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

    public String getName() {
        return name;
    }
    public void setName(String name) {  
        this.name = name;
    }
   
    public CustomerUserRole getRole() {
        return role;
    }
    public void setRole(CustomerUserRole role) {
        this.role = role;   

    }

    
}