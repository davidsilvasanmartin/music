package dev.davidsilva.music.security.auth;

import dev.davidsilva.music.security.user.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * This is the class that's used by Spring Security. It wraps our database user. If
 * we add other methods of authentication such as "Sign in with Google", we will
 * need to create another implementation of UserDetails
 */
public class DbUserDetails implements UserDetails, Serializable {
    private final int id;
    private final String password;
    private final String username;
    private final Collection<? extends GrantedAuthority> authorities;
    private final boolean isEnabled;

    public DbUserDetails(User user) {
        this.id = user.getId();
        this.password = user.getPassword();
        this.username = user.getUsername();
        this.authorities = user.getPermissions().stream().map(p -> new SimpleGrantedAuthority(p.getName()))
                .collect(Collectors.toList());
        this.isEnabled = user.getIsEnabled();
    }

    public int getId() {
        return id;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
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
        return isEnabled;
    }
}