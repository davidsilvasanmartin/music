package dev.davidsilva.music.security.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * TODO using DaoAuthenticationProvider at the moment, this class may be removed ...
 */
//@Component
@RequiredArgsConstructor
public class DbUserAuthenticationProvider implements AuthenticationProvider {
    // This class would work just injecting UserDetailsService. However, I'm choosing to inject the more specific class.
    // I think this is better because in the future we will have several UserDetailsService beans, but we'll see
    private final DbUserUserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    /**
     * This AuthenticationProvider will try to authenticate any UsernamePasswordAuthenticationTokens it receives.
     * If adding more AuthenticationProviders that deal with the same token but in a different way, the code in
     * `authenticate` has to be changed so it returns `null` for the cases that should not be handled by this
     * AuthenticationProvider
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username;
        String password;
        try {
            username = authentication.getName();
            password = authentication.getCredentials().toString();
        } catch (Exception e) {
            throw new BadCredentialsException("Invalid credentials");
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        if (passwordEncoder.matches(password, userDetails.getPassword())) {
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
