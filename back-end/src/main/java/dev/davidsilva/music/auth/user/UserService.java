package dev.davidsilva.music.auth.user;

import dev.davidsilva.music.audit.AuditLogAction;
import dev.davidsilva.music.audit.AuditLogService;
import dev.davidsilva.music.auth.role.Role;
import dev.davidsilva.music.auth.role.RoleRepository;
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
        this.auditLogService.log(
                AuditLogAction.CREATE.toString(),
                "USER",
                String.valueOf(newUser.getId()),
                null,
                null,
                // TODO The logged-in user's id
                null,
                null,
                // TODO check what this logs
                newUser.toString(),
                null
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
