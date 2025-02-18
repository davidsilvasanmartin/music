package dev.davidsilva.music.security.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * TODO maybe just use DaoAuthenticationProvider, which has mitigation measures against some attacks
 */
@Component
@RequiredArgsConstructor
public class UserAuthenticationProvider implements AuthenticationProvider {
    // This class would work just injecting UserDetailsService. However, I'm choosing to inject the more specific class.
    // I think this will be better because in the future we will have several UserDetailsService beans, but we'll see
    private final UserUserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        if (passwordEncoder.matches(password, userDetails.getPassword())) {
            // According to AbstractUserDetailsAuthenticationProvider, we should use authentication.getCredentials()
            // here (the plain-text password) so that the password is available for possible successive
            // authentication attempts. But, according to Daniel Garnier-Moiroux, it can (should) be null
            return UsernamePasswordAuthenticationToken.authenticated(userDetails, null, userDetails.getAuthorities());
        } else {
            throw new BadCredentialsException("Invalid credentials");
        }
    }

    @Override
    public boolean supports(Class<?> authenticationType) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authenticationType);
    }
}
