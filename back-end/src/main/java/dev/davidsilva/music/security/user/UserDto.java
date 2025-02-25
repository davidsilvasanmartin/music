package dev.davidsilva.music.security.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import dev.davidsilva.music.security.role.RoleDto;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

// The following annotation prevents the "password" field from going to the frontend when we GET a user (because it will be null).
// Note that the rest of the fields should not be null
@JsonInclude(JsonInclude.Include.NON_NULL)
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
