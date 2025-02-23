package dev.davidsilva.music.security.role;

import dev.davidsilva.music.security.permission.PermissionDto;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
public class RoleDto {
    private String name;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Set<PermissionDto> permissions;
}
