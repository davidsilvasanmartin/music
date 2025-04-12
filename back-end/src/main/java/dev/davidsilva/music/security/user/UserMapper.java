package dev.davidsilva.music.security.user;

import dev.davidsilva.music.security.role.RoleMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {RoleMapper.class})
public abstract class UserMapper {
    @Mapping(target = "isEnabled", source = "enabled")
    public abstract User toEntity(UserDto dto);

    @Mapping(target = "enabled", source = "isEnabled")
    @Mapping(target = "password", ignore = true)
    public abstract UserDto toDto(User entity);
}
