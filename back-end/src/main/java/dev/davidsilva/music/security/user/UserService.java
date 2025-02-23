package dev.davidsilva.music.security.user;

import dev.davidsilva.music.audit.AuditLogAction;
import dev.davidsilva.music.audit.AuditLogService;
import dev.davidsilva.music.security.role.Role;
import dev.davidsilva.music.security.role.RoleRepository;
import dev.davidsilva.music.utils.ListMapper;
import dev.davidsilva.music.utils.PaginatedResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * When deleting users, check
 * - You can't delete yourself (maybe yes?)
 * - You can't delete the last admin user
 */

/**
 * TODO need to figure out which functions are for internal consumption by Spring Security
 * (so they return User) and which are for API clients (so they return UserDto)
 */

@Service
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final UserDtoMapper userDtoMapper;
    private final ListMapper<User, UserDto> listMapper;
    private final RoleRepository roleRepository;
    private final AuditLogService auditLogService;

    public UserService(UserRepository userRepository, UserDtoMapper userDtoMapper, RoleRepository roleRepository, AuditLogService auditLogService) {
        this.userRepository = userRepository;
        this.userDtoMapper = userDtoMapper;
        this.listMapper = (users) -> users.stream().map(userDtoMapper::toDto).toList();
        this.roleRepository = roleRepository;
        this.auditLogService = auditLogService;
    }

    public PaginatedResponse<UserDto> findAll(UserSpecification specification, Pageable pageable) {
        Page<User> usersPage = userRepository.findAll(specification, pageable);
        return PaginatedResponse.fromPage(usersPage, listMapper);
    }

    public PaginatedResponse<UserDto> findAll(Pageable pageable) {
        Page<User> usersPage = userRepository.findAll(pageable);
        return PaginatedResponse.fromPage(usersPage, listMapper);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public UserDto createUser(UserDto user) {
        // TODO add validation logic here
        User newUser = userDtoMapper.toEntity(user);
        newUser = userRepository.save(newUser);

        this.auditLogService.log(
                AuditLogAction.CREATE.toString(),
                "USER",
                String.valueOf(newUser.getId()),
                null,
                // TODO check what this logs
                newUser,
                null
        );
        return userDtoMapper.toDto(newUser);
    }

    public void addRoleToUser(String username, String roleName) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new RuntimeException("Role not found"));
        user.getRoles().add(role);
        userRepository.save(user);
    }

    public long countUsers() {
        return userRepository.count();
    }
}
