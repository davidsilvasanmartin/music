package dev.davidsilva.music.auth;

import dev.davidsilva.music.audit.AuditLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final AuditLogService auditLogService;

    @Transactional(readOnly = true)
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Transactional
    public User createUser(User user) {
        // TODO add validation logic here
        User newUser = userRepository.save(user);
        // TODO the current user's id
        this.auditLogService.log(
                AuthAuditLogActions.AUTH_USER_CREATED.toString(),
                "USER",
                String.valueOf(newUser.getId()),
                null,
                null,
                // TODO check what this logs
                newUser.toString(),
                "New user account created"
        );
        return newUser;
    }

    @Transactional
    public void addRoleToUser(String username, String roleName) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Role role = roleRepository.findByRoleName(roleName)
                .orElseThrow(() -> new RuntimeException("Role not found"));
        user.getRoles().add(role);
        userRepository.save(user);
    }
}
