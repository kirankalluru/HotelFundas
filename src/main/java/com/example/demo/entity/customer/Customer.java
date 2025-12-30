package com.example.demo.entity.customer;

import com.example.demo.entity.common.BaseEntity;
import com.example.demo.entity.common.Address;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import com.example.demo.converter.AddressConverter;

@Entity
@Table(name = "customers")
public class Customer extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "customer_seq_gen")
    @SequenceGenerator(
            name = "customer_seq_gen",
            sequenceName = "customer_seq",
            allocationSize = 108,
            initialValue = 100000
    )
    private Long id;

    // ✅ ADDED: branchId
    @Column(name = "branch_id", nullable = false)
    private Long branchId;

    @Column(nullable = false, unique = true)
    private String name;

    private String tradeName;
    private String code;

    @Column(length = 15)
    private String gstin;

    @Column(length = 10)
    private String pan;

    @Column(name = "driving_license")
    private String drivingLicense;


    private String phone;
    private String email;

    @Convert(converter = AddressConverter.class)
    @Column(columnDefinition = "json")
    private Address billingAddress;

    @Convert(converter = AddressConverter.class)
    @Column(columnDefinition = "json")
    private Address shippingAddress;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ContactPerson> contactPersons = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CustomerType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CustomerStatus status = CustomerStatus.ACTIVE;

    private boolean isActive;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CustomerUser> users = new ArrayList<>();

    // ===== Getters & Setters =====

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    // ✅ branchId getter & setter
    public Long getBranchId() {
        return branchId;
    }

    public void setBranchId(Long branchId) {
        this.branchId = branchId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTradeName() {
        return tradeName;
    }

    public void setTradeName(String tradeName) {
        this.tradeName = tradeName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public List<ContactPerson> getContactPersons() {
        return contactPersons;
    }

    public void setContactPersons(List<ContactPerson> contactPersons) {
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

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getDrivingLicense() {
        return drivingLicense;
    }

    public void setDrivingLicense(String drivingLicense) {
        this.drivingLicense = drivingLicense;
    }

}
