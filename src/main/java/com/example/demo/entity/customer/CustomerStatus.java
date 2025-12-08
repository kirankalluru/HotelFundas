package com.example.demo.entity.customer;

public enum CustomerStatus {
    ACTIVE,         // Customer is active and can place orders
    INACTIVE,       // Customer is temporarily inactive
    BLOCKED,        // Customer is blocked from placing orders
    PENDING,        // Customer registration is pending approval
    ARCHIVED        // Old/inactive customer moved to archive
} 