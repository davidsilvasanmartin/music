package dev.davidsilva.music.security.auth;

import dev.davidsilva.music.security.permission.Permission;
import dev.davidsilva.music.security.user.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * This is the class that's used by Spring Security. It wraps our database user. If
 * we add other methods of authentication such as "Sign in with Google", we will
 * need to create another implementation of UserDetails
 * <p>
 * TODO think whether this and related classes should be named with "DbUser" prefix instead of just "User" prefix
 */
@Getter
public class DbUserDetails implements UserDetails {
    // TODO not sure if this should be here. We need it to get the authenticated user's id
    private final User user;

    public DbUserDetails(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<Permission> permissions = user.getPermissions();
        if (permissions == null) {
            return Collections.emptyList();
        }

        return permissions.stream().map(p -> new SimpleGrantedAuthority(p.getPermissionName()))
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Locked indicates an account has been automatically suspended due to invalid login attempts.
     * Usually the passage of time or (less often) requesting manual unlocking is required to release it.
     * A locked account usually happens when a user triggers security policies—for example, after too
     * many consecutive failed login attempts. Even if the account is enabled, a locked account will
     * prevent the user from signing in.
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Disabled indicates an account has been administratively or automatically disabled for some reason.
     * Usually some action is required to release it. - When a user is not enabled, sign-in is prevented
     * regardless of other conditions. It often reflects a state set by an administrator (for example,
     * when an active account is deactivated) or based on user activation (such as confirming an email
     * address).
     */
    @Override
    public boolean isEnabled() {
        return user.getIsEnabled();
    }
}