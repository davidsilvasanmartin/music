package dev.davidsilva.music.security.user;

import dev.davidsilva.music.security.role.RoleDto;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
public class UserDto {
    private String username;
    // Only used for creating or modifying a user
    private String password;
    private String email;
    private boolean isEnabled;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Set<RoleDto> roles;
}
