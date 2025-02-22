package dev.davidsilva.music.security.user;

import dev.davidsilva.music.security.role.Role;
import dev.davidsilva.music.utils.DtoMapper;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class UserDtoMapper implements DtoMapper<UserDto, User> {
    @Override
    public User toEntity(UserDto userDto) {
        throw new UnsupportedOperationException("Not supported yet");
    }

    @Override
    public UserDto toDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setUsername(user.getUsername());
        userDto.setEmail(user.getEmail());
        userDto.setEnabled(user.getIsEnabled());
        userDto.setRoles(user.getRoles().stream().map(Role::getRoleName).collect(Collectors.toSet()));
        userDto.setCreatedAt(user.getCreatedAt());
        userDto.setUpdatedAt(user.getUpdatedAt());
        return userDto;
    }
}
