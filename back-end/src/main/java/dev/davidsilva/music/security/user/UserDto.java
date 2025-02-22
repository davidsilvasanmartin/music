package dev.davidsilva.music.security.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
@Data
public class UserDto {
    private String username;
    private String email;
    private boolean isEnabled;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    // This DTO will be used by admins to see the list of users, their configs etc,
    // so there is no need to get the list of permissions.
    private Set<String> roles;
}
