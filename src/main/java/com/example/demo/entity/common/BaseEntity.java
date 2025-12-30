package com.example.demo.entity.common;

import javax.persistence.*;
import org.hibernate.annotations.*;
import java.time.LocalDateTime;
import com.example.demo.context.UserContext;

@MappedSuperclass
@FilterDef(name = "tenantFilter", parameters = @ParamDef(name = "companyId", type = "long"))
@Filter(name = "tenantFilter", condition = "company_id = :companyId")
@SQLDelete(sql = "UPDATE #{#entityName} SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
@Where(clause = "deleted_at IS NULL")
public abstract class BaseEntity {

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "updated_by")
    private Long updatedBy;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();

        // Safely get user ID from UserContext
        UserContext userContext = UserContext.getCurrentUser();
        if (userContext != null) {
            createdBy = userContext.getUserId();
            updatedBy = userContext.getUserId();
        } else {
            // Default value when UserContext is not set
            createdBy = 1L;  // System user
            updatedBy = 1L;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();

        // Safely get user ID from UserContext for updates
        UserContext userContext = UserContext.getCurrentUser();
        if (userContext != null) {
            updatedBy = userContext.getUserId();
        }
    }

    // Getters and setters remain the same
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public Long getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Long updatedBy) {
        this.updatedBy = updatedBy;
    }

    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }
}
