package dev.davidsilva.music.security.user;

import dev.davidsilva.music.security.role.*;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.Set;

@Mapper(componentModel = "spring", uses = {RoleMapper.class})
public abstract class UserMapper {
    @Autowired
    protected RoleRepository roleRepository;

    @Mapping(target = "isEnabled", source = "enabled")
    @Mapping(target = "roles", ignore = true)
    public abstract User toEntity(UserDto dto);

    @Mapping(target = "enabled", source = "isEnabled")
    @Mapping(target = "password", ignore = true)
    public abstract UserDto toDto(User entity);

    @AfterMapping
    protected void mapRolesToEntity(UserDto dto, @MappingTarget User entity) {
        if (dto.getRoles() != null) {
            Set<Role> roles = new HashSet<>();
            for (RoleDto roleDto : dto.getRoles()) {
                roles.add(
                        roleRepository
                                .findByName(roleDto.getName())
                                .orElseThrow(() -> new RoleNotFoundException(roleDto.getName()))
                );
            }
            entity.setRoles(roles);
        }
    }

}
