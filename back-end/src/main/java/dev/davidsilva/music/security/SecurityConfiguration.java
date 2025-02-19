package dev.davidsilva.music.security;

import dev.davidsilva.music.audit.AuditLogService;
import dev.davidsilva.music.security.user.UserAuthenticationProvider;
import dev.davidsilva.music.security.user.UserUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

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

/**
 * TODO watch this https://www.youtube.com/watch?v=96vK5BDpT7g
 * TODO if going with OAuth2+JWT, we probably don't have to implement CSRF tokens but we have to check CORS and CSPs
 */

@Configuration
//@EnableWebSecurity
@EnableWebSecurity(debug = true)
@RequiredArgsConstructor
public class SecurityConfiguration {
    //    private final TokenProvider tokenProvider;
    private final AuditLogService auditLogService;
    private final UserUserDetailsService userUserDetailsService;
    private final UserAuthenticationProvider userAuthenticationProvider;
    private final PasswordEncoder passwordEncoder;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final CustomAuthenticationFailureHandler customAuthenticationFailureHandler;


    // TODO not used. This would produce something very similar to userAuthenticationProvider
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userUserDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }

    public AuthenticationManager authenticationManager() {
        return new ProviderManager(userAuthenticationProvider);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                // We don't need CSRF because our token is invulnerable
                .csrf(AbstractHttpConfigurer::disable)
                // Enable CORS using the custom configuration below. <-- TODO is this true ??
                .cors(Customizer.withDefaults())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationManager(authenticationManager())
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
                        .requestMatchers("/users/**").hasAnyAuthority("ADMIN")
                        .requestMatchers("/songs/**").hasAnyRole("ADMIN")
                        .anyRequest().authenticated()
                )
                // Specifying the customAuthenticationEntryPoint here will make it handle for example the scenario where the
                // username is not found. That scenario is not handled just by having the below configuration .exceptionHandling(...)
                .httpBasic(c -> c.authenticationEntryPoint(customAuthenticationEntryPoint))
                .formLogin(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)
                .exceptionHandling(c -> c
                        .authenticationEntryPoint(customAuthenticationEntryPoint)
                        .accessDeniedHandler(customAccessDeniedHandler)
                );

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

    // TODO we need to log authentication success events (creation of tokens)
}
