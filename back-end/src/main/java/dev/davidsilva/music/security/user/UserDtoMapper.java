package dev.davidsilva.music.security.user;

import dev.davidsilva.music.security.role.*;
import dev.davidsilva.music.utils.DtoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserDtoMapper implements DtoMapper<UserDto, User> {
    private final RoleDtoMapper roleDtoMapper;
    private final RoleRepository roleRepository;

    @Override
    public User toEntity(UserDto userDto) {
        // The roles have to exist
        Set<Role> roles = new HashSet<>();
        for (RoleDto roleDto : userDto.getRoles()) {
            roles.add(
                    roleRepository
                            .findByName(roleDto.getName())
                            .orElseThrow(() -> new RoleNotFoundException(roleDto.getName()))
            );
        }

        User user = new User();
        user.setUsername(userDto.getUsername());
        if (userDto.getPassword() != null) {
            user.setPassword(userDto.getPassword());
        }
        user.setEmail(userDto.getEmail());
        user.setIsEnabled(userDto.isEnabled());
        user.setRoles(roles);
        user.setCreatedAt(userDto.getCreatedAt());
        user.setUpdatedAt(userDto.getUpdatedAt());
        return user;
    }

    @Override
    public UserDto toDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setUsername(user.getUsername());
        userDto.setEmail(user.getEmail());
        userDto.setEnabled(user.getIsEnabled());
        userDto.setRoles(
                user.getRoles().stream().map(roleDtoMapper::toDto).collect(Collectors.toSet())
        );
        userDto.setCreatedAt(user.getCreatedAt());
        userDto.setUpdatedAt(user.getUpdatedAt());
        return userDto;
    }
}
