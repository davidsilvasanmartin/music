package dev.davidsilva.music.security.role;

import dev.davidsilva.music.security.permission.PermissionDtoMapper;
import dev.davidsilva.music.utils.DtoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class RoleDtoMapper implements DtoMapper<RoleDto, Role> {
    private final PermissionDtoMapper permissionDtoMapper;

    @Override
    public Role toEntity(RoleDto dto) {
        Role entity = new Role();
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setCreatedAt(dto.getCreatedAt());
        entity.setUpdatedAt(dto.getUpdatedAt());
        entity.setPermissions(
                dto.getPermissions().stream().map(permissionDtoMapper::toEntity).collect(Collectors.toSet())
        );
        return entity;
    }

    @Override
    public RoleDto toDto(Role entity) {
        RoleDto dto = new RoleDto();
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        dto.setPermissions(
                entity.getPermissions().stream().map(permissionDtoMapper::toDto).collect(Collectors.toSet())
        );
        return dto;
    }
}
