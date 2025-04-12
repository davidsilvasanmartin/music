package dev.davidsilva.music.security.user;

import dev.davidsilva.music.audit.AuditLogAction;
import dev.davidsilva.music.audit.AuditLogService;
import dev.davidsilva.music.security.role.Role;
import dev.davidsilva.music.security.role.RoleNotFoundException;
import dev.davidsilva.music.security.role.RoleRepository;
import dev.davidsilva.music.utils.ListMapper;
import dev.davidsilva.music.utils.PaginatedResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final UserMapper userMapper;
    private final ListMapper<User, UserDto> listMapper;
    private final RoleRepository roleRepository;
    private final AuditLogService auditLogService;

    public UserService(UserRepository userRepository, UserMapper userMapper, RoleRepository roleRepository, AuditLogService auditLogService) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.listMapper = (users) -> users.stream().map(userMapper::toDto).toList();
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

    public UserDto findByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(userMapper::toDto).orElseThrow(() -> new UserNotFoundException(username));
    }

    public void deleteByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
        if (countAdminUsers() == 1 && isAdmin(username)) {
            auditLogService.log(
                    "DELETE_FAILED",
                    "USER",
                    username,
                    user,
                    null,
                    "Failed to delete user: Cannot delete last admin user"
            );
            throw new CannotDeleteLastAdminUserException();
        }
        try {
            userRepository.delete(user);
            auditLogService.log(
                    "DELETE",
                    "USER",
                    username,
                    user,
                    null,
                    "User deleted successfully"
            );
        } catch (Exception e) {
            auditLogService.log(
                    "DELETE_FAILED",
                    "USER",
                    username,
                    user,
                    null,
                    "Failed to delete user: " + e.getMessage()
            );
            throw e;
        }

    }

    public int countAdminUsers() {
        return userRepository.countAdminUsers();
    }

    public boolean isAdmin(String username) {
        User user = userRepository
                .findByUsernameWithEagerlyFetchedPermissions(username)
                .orElseThrow(() -> new UserNotFoundException(username));
        return user.getPermissions().stream().anyMatch(permission -> permission.getName().equals("ADMIN"));
    }

    public UserDto createUser(UserDto user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            auditLogService.log(
                    "CREATE_FAILED",
                    "USER",
                    user.getUsername(),
                    null,
                    user,
                    "Failed to create user: username already exists"
            );
            throw new UserAlreadyExistsException(user.getUsername());
        }

        // TODO add validation logic here (or in the controller with Bean Validation)
        User newUser = userMapper.toEntity(user);

        try {
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
            return userMapper.toDto(newUser);
        } catch (Exception e) {
            auditLogService.log(
                    "CREATE_FAILED",
                    "USER",
                    user.getUsername(),
                    null,
                    user,
                    "Failed to create user: " + e.getMessage()
            );
            throw new RuntimeException(e);
        }
    }

    public void addRoleToUser(String username, String roleName) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new RoleNotFoundException(roleName));

        try {
            // TODO review properties that are logged
            user.getRoles().add(role);
            userRepository.save(user);
            auditLogService.log(
                    "ADD_ROLE",
                    "USER",
                    username,
                    user.getRoles().stream()
                            .map(Role::getName)
                            .toList(),
                    roleName,
                    "Role '" + roleName + "' added to user"
            );
        } catch (Exception e) {
            auditLogService.log(
                    "ADD_ROLE_FAILED",
                    "USER",
                    username,
                    user.getRoles().stream()
                            .map(Role::getName)
                            .toList(),
                    roleName,
                    "Failed to add role: " + e.getMessage()
            );
            throw e;
        }

    }
}
