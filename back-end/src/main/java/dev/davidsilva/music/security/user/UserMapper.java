package dev.davidsilva.music.security.user;

import dev.davidsilva.music.security.role.RoleMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * TODO I'm seeing these warnings
 * [WARNING] /Users/dev/Developer/music/back-end/src/main/java/dev/davidsilva/music/security/user/UserMapper.java:[10,26] Unmapped target properties: "id, permissions".
 * [WARNING] /Users/dev/Developer/music/back-end/src/main/java/dev/davidsilva/music/security/permission/PermissionMapper.java:[7,16] Unmapped target property: "id".
 * [WARNING] /Users/dev/Developer/music/back-end/src/main/java/dev/davidsilva/music/security/role/RoleMapper.java:[8,10] Unmapped target property: "id".
 */

@Mapper(componentModel = "spring", uses = {RoleMapper.class})
public abstract class UserMapper {
    @Mapping(target = "isEnabled", source = "enabled")
    public abstract User toEntity(UserDto dto);

    @Mapping(target = "enabled", source = "isEnabled")
    @Mapping(target = "password", ignore = true)
    public abstract UserDto toDto(User entity);
}
