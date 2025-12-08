package com.example.demo.service;

import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Random;

@Service
public class ReferenceNumberGenerator {
    private static final Random random = new Random();
    
    public String generateTripReference(Long branchId) {
        return generateReference("TRP", branchId);
    }
    
    public String generateVisitReference(Long branchId) {
        return generateReference("VST", branchId);
    }
    
    public String generateOrderReference(Long branchId) {
        return generateReference("ORD", branchId);
    }
    
    public String generateInvoiceReference(Long branchId) {
        return generateReference("INV", branchId);
    }
    
    public String generateInvoiceItemReference(Long branchId) {
        return generateReference("INI", branchId);
    }
    
    private String generateReference(String prefix, Long branchId) {
        LocalDateTime now = LocalDateTime.now();
        String monthYear = String.format("%02d%02d", now.getMonthValue(), now.getYear() % 100);
        String randomNum = String.format("%04d", random.nextInt(10000));
        
        return String.format("%s-%03d-%s-%s", 
            prefix,           // Type prefix (e.g., TRP, VST)
            branchId,        // Branch ID padded to 3 digits
            monthYear,       // MMYY format
            randomNum        // 4 digit random number
        );
    }
} 