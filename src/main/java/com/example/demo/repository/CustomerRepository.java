package com.example.demo.repository;

import com.example.demo.entity.customer.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    
    @Query("SELECT c FROM Customer c WHERE c.isActive = true")
    List<Customer> findAllActive();

    @Query("SELECT c FROM Customer c WHERE c.branch.id = :branchId AND c.isActive = true")
    List<Customer> findActiveByBranchId(@Param("branchId") Long branchId);

    @Query("SELECT c FROM Customer c WHERE " +
           "LOWER(c.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(c.code) LIKE LOWER(CONCAT('%', :searchTerm, '%')) AND " +
           "c.isActive = true")
    List<Customer> searchActiveCustomers(@Param("searchTerm") String searchTerm);

    Optional<Customer> findByCode(String code);

    boolean existsByCode(String code);

    @Query("SELECT c FROM Customer c WHERE c.id = :id")
    Customer getCustomerById(@Param("id") Long id);

    // Page<Customer> findByNameContainingIgnoreCaseOrCodeContainingIgnoreCase(
    //     String name, String code, Pageable pageable);

    List<Customer> findByNameContainingIgnoreCase(String name);

    Page<Customer> findByNameContainingIgnoreCase(String name, Pageable pageable);

    Optional<Customer> findByPhone(String phone);

    Optional<Customer> findByEmail(String email);   
}