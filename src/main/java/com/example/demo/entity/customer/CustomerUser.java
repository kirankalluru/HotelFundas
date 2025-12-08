package com.example.demo.entity.customer;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import com.example.demo.entity.common.*;

@Entity
@Table(name = "customer_users")
public class CustomerUser extends BaseEntity { 
    @Id
     @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "customer_user_seq_gen")
    @SequenceGenerator(name = "customer_user_seq_gen", sequenceName = "customer_user_seq", allocationSize = 101, initialValue = 112301)
    private Long id;

    @Email
    @NotBlank
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank
    @Column(nullable = false)
    private String password;

    @Column(name = "phone")
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CustomerUserRole role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatus status = UserStatus.ACTIVE;

    private String name;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    // Getters and setters
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

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public CustomerUserRole getRole() {
        return role;
    }

    public void setRole(CustomerUserRole role) {
        this.role = role;
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

     
    public Customer getCustomer(){
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }  
    
    public void setName(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }   
}