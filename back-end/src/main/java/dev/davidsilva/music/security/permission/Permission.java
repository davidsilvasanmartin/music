package dev.davidsilva.music.security.permission;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "auth_permissions")
@Data
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "permission_id")
    private int id;

    @Column(name = "permission_name", unique = true, nullable = false)
    private String permissionName;

    private String description;

    @Column(name = "created_at", insertable = false)
    private LocalDateTime createdAt;

//    @ManyToMany(mappedBy = "permissions")
//    private Set<Role> roles;
}