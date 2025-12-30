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

    // ================= ACTIVE CUSTOMERS =================

    @Query("SELECT c FROM Customer c WHERE c.isActive = true")
    Page<Customer> findAllActive(Pageable pageable);

    @Query("SELECT c FROM Customer c WHERE c.branchId = :branchId AND c.isActive = true")
    Page<Customer> findActiveByBranchId(
            @Param("branchId") Long branchId,
            Pageable pageable
    );

    // ================= SEARCH WITH PAGINATION =================

    @Query(
            "SELECT c FROM Customer c " +
                    "WHERE (" +
                    "LOWER(c.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
                    "OR LOWER(c.code) LIKE LOWER(CONCAT('%', :searchTerm, '%'))" +
                    ") " +
                    "AND c.isActive = true"
    )
    Page<Customer> searchActiveCustomers(
            @Param("searchTerm") String searchTerm,
            Pageable pageable
    );

    // ================= SIMPLE SEARCH =================

    List<Customer> findByNameContainingIgnoreCase(String name);

    Page<Customer> findByNameContainingIgnoreCase(
            String name,
            Pageable pageable
    );

    // ================= LOOKUPS =================

    Optional<Customer> findByCode(String code);

    boolean existsByCode(String code);

    Optional<Customer> findByPhone(String phone);

    Optional<Customer> findByEmail(String email);

    // ================= OPTIONAL (NOT REQUIRED BUT SAFE) =================

    @Query("SELECT c FROM Customer c WHERE c.id = :id")
    Optional<Customer> getCustomerById(@Param("id") Long id);
}
