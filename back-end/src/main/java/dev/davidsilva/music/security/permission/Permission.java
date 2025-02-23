package dev.davidsilva.music.security.permission;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * TODO when making these editable: DO NOT allow creating permissions that start with ROLE_
 */

@Entity
@Table(name = "auth_permissions")
@Data
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "permission_id")
    private int id;

    @Column(name = "permission_name", unique = true, nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "created_at", insertable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    @PreUpdate
    private void updateTimestamp() {
        this.updatedAt = LocalDateTime.now();
    }
}