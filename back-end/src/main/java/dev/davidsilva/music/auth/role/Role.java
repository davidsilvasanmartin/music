package dev.davidsilva.music.auth.role;

import dev.davidsilva.music.auth.permission.Permission;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "auth_roles")
@Data
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private int id;

    @Column(name = "role_name", unique = true, nullable = false)
    private String roleName;

    private String description;

    @Column(name = "created_at", insertable = false)
    private LocalDateTime createdAt;

//    @ManyToMany(mappedBy = "roles")
//    private Set<User> users;

    @ManyToMany
    @JoinTable(
            name = "auth_role_permissions",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id")
    )
    private Set<Permission> permissions;
}
