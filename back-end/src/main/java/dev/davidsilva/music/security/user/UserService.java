package dev.davidsilva.music.security.user;

import dev.davidsilva.music.audit.AuditLogAction;
import dev.davidsilva.music.audit.AuditLogService;
import dev.davidsilva.music.security.role.Role;
import dev.davidsilva.music.security.role.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * When deleting users, check
 * - You can't delete yourself (maybe yes?)
 * - You can't delete the last admin user
 */

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final AuditLogService auditLogService;

    @Transactional
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Transactional
    public User createUser(User user) {
        // TODO add validation logic here
        User newUser = userRepository.save(user);

        // TODO better way of doing this ?
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        int authenticatedUserId = ((UserUserDetails) authentication.getPrincipal()).getUser().getId();

        this.auditLogService.log(
                AuditLogAction.CREATE.toString(),
                "USER",
                String.valueOf(newUser.getId()),
                // TODO The logged-in user's id
                authenticatedUserId,
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

    @Transactional
    public long countUsers() {
        return userRepository.count();
    }
}
