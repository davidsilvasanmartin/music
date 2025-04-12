package dev.davidsilva.music.security.role;

import dev.davidsilva.music.security.permission.PermissionMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {PermissionMapper.class})
public interface RoleMapper {
    Role toEntity(RoleDto dto);

    RoleDto toDto(Role entity);
}
