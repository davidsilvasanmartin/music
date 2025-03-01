package dev.davidsilva.music.security.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class AuthenticationManagerConfiguration {
    //    private final DbUserAuthenticationProvider dbUserAuthenticationProvider;
    private final DbUserUserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    // TODO decide which is better... I think DaoAuthenticationProvider wins. IT handles more cases (such as having
    //  provided no credentials), and protects against timing attacks. Check source code.
    // @Bean
//    public AuthenticationManager authenticationManager() {
//        // The AuthenticationManager (and thus the DbUserAuthenticationProvider) is only used at the moment in the
//        // AuthController, but not in any of the Spring Security filters. This is because the only way to login is
//        // by POSTing the credentials to /auth/login, and no filter allows for logging in. The only filter we have
//        // that performs authentication is based on JWT and thus does not require username/password
//        return new ProviderManager(dbUserAuthenticationProvider);
//    }

    @Bean
    public AuthenticationManager authenticationManager2() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return new ProviderManager(provider);
    }
}
