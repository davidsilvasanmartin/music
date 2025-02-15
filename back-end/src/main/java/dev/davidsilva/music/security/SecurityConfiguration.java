package dev.davidsilva.music.security;

import dev.davidsilva.music.audit.AuditLogService;
import dev.davidsilva.music.security.user.UserService;
import dev.davidsilva.music.security.user.UserUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.HashMap;
import java.util.Map;

/**
 * Inspiration taken from https://chsariotsolutions.com/blog/post/angular-2-spring-boot-jwt-cors_part2/
 * which uses https://www.toptal.com/java/rest-security-with-jwt-spring-security-and-java
 * which uses https://github.com/jwtk/jjwt
 * <p>
 * See also the following links:
 * https://www.baeldung.com/spring-security-httpsecurity-vs-websecurity
 * https://spring.io/blog/2022/02/21/spring-security-without-the-websecurityconfigureradapter
 * https://docs.spring.io/spring-security/reference/5.8/migration/servlet/config.html
 */

@Configuration
//@EnableWebSecurity
@EnableWebSecurity(debug = true)
@RequiredArgsConstructor
public class SecurityConfiguration {
    //    private final TokenProvider tokenProvider;
    private final UserService usersService; // Inject UsersService that stores and loads users from DB
    private final AuditLogService auditLogService;
    private final UserUserDetailsService userUserDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        String idForEncode = "bcrypt";
        Map<String, PasswordEncoder> encoders = new HashMap<>();
        encoders.put(idForEncode, new BCryptPasswordEncoder());
        return new DelegatingPasswordEncoder(idForEncode, encoders);
    }

    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userUserDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    public AuthenticationManager authenticationManager() {
        return new ProviderManager(authenticationProvider());
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                // We don't need CSRF because our token is invulnerable
                .csrf(AbstractHttpConfigurer::disable)
                // Enable CORS using the custom configuration below.
                .cors(Customizer.withDefaults())
                // Use the stateless session management
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // Register our custom authentication provider
                .authenticationManager(authenticationManager())
                // Define authorization rules:
                .authorizeHttpRequests(auth -> auth
                        // The authentication-related endpoints are open
                        .requestMatchers("/api/auth/**").permitAll()
                        // Permit all OPTIONS requests (e.g., for CORS preflight).
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        // TODO internal errors (500) are not being sent to the user if I don't add matchers for "/error".
                        // They also do not appear in the running logs, unless I enable TRACE. Need to find
                        // a better solution for this.
                        .requestMatchers("/error").permitAll()
                        .requestMatchers("/api/error").permitAll()
                        .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults())
                .formLogin(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable);

        // Optionally add JWT filter(s) here if using JWT authentication

        return httpSecurity.build();
    }

//    @Bean
//    public WebSecurityCustomizer webSecurityCustomizer() {
//        // We would add an ignore rule for static resources here, if we had them
//        return (web) -> web.ignoring().requestMatchers(HttpMethod.OPTIONS, "/**");
//    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // Allow all origins for demonstration; restrict in production.
        configuration.addAllowedOriginPattern("*");
        configuration.addAllowedMethod("*");
        configuration.addAllowedHeader("*");
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public ApplicationListener<AuthenticationSuccessEvent> authenticationSuccessListener() {
        return event -> {
            String username = event.getAuthentication().getName();
            // Depending on your UsersService implementation, you might retrieve user details (such as id).
            // Here, we pass null for userId and other values as placeholders.
            auditLogService.log(
                    // TODO add constant or use auth log table
                    "AUTH_SUCCESS",
                    "USER_AUTH",
                    // TODO user id
                    null, // entityId1 (if needed)
                    null, // entityId2
                    null, // entityId3
                    null, // userId (extract if available)
                    event.getAuthentication().getDetails(), // oldValue (or additional details)
                    null, // newValue
                    "Authentication succeeded for user: " + username
            );
        };
    }

    @Bean
    public ApplicationListener<AuthenticationFailureBadCredentialsEvent> authenticationFailureListener() {
        return event -> {
            String username = event.getAuthentication().getName();
            String error = event.getException().getMessage();
            auditLogService.log(
                    "AUTH_FAILURE",
                    "USER_AUTH",
                    null, // entityId1
                    null, // entityId2
                    null, // entityId3
                    null, // userId (if available)
                    null, // oldValue
                    null, // newValue
                    "Authentication failed for user: " + username + ". Error: " + error
            );
        };
    }

}
