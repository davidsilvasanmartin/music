package dev.davidsilva.music.security.user;

import dev.davidsilva.music.security.role.RoleDtoMapper;
import dev.davidsilva.music.utils.DtoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserDtoMapper implements DtoMapper<UserDto, User> {
    private final RoleDtoMapper roleDtoMapper;

    @Override
    public User toEntity(UserDto userDto) {
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setIsEnabled(userDto.isEnabled());
        user.setRoles(
                userDto.getRoles().stream().map(roleDtoMapper::toEntity).collect(Collectors.toSet())
        );
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
