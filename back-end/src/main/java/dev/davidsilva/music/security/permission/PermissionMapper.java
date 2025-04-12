package dev.davidsilva.music.security.permission;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permission toEntity(PermissionDto dto);

    PermissionDto toDto(Permission entity);
}
