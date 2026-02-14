package com.example.demo.entity.common;

import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.*;
import java.time.LocalDateTime;
import com.example.demo.context.UserContext;

@Setter
@Getter
@MappedSuperclass
@FilterDef(name = "tenantFilter", parameters = @ParamDef(name = "companyId", type = "long"))
@Filter(name = "tenantFilter", condition = "company_id = :companyId")
@SQLDelete(sql = "UPDATE #{#entityName} SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
@Where(clause = "deleted_at IS NULL")
public abstract class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // Getters and setters remain the same
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

}
