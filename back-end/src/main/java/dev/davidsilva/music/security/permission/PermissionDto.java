package dev.davidsilva.music.security.permission;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PermissionDto {
    private String name;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
