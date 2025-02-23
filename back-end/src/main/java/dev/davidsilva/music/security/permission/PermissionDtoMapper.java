package dev.davidsilva.music.security.permission;

import dev.davidsilva.music.utils.DtoMapper;
import org.springframework.stereotype.Component;

@Component
public class PermissionDtoMapper implements DtoMapper<PermissionDto, Permission> {
    @Override
    public Permission toEntity(PermissionDto dto) {
        Permission entity = new Permission();
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setCreatedAt(dto.getCreatedAt());
        entity.setUpdatedAt(dto.getUpdatedAt());
        return entity;
    }

    @Override
    public PermissionDto toDto(Permission entity) {
        PermissionDto dto = new PermissionDto();
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }
}
