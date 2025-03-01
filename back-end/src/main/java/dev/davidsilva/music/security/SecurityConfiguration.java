package dev.davidsilva.music.security;

import dev.davidsilva.music.security.auth.CustomAccessDeniedHandler;
import dev.davidsilva.music.security.auth.CustomAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
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
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
//    private final DbUserDetailsService userDetailsService;

    /**
     * TODO somewhat we are still redirecting to /error when there is any error (TODO find a way to test this and fix).
     * One way to force this error is to comment the permitAll() for GET requests below and trying to GET /albums with
     * a valid JWT token of a valid authenticated user
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                // TODO enable csrf
                .csrf(AbstractHttpConfigurer::disable)
                .cors(c -> c.configurationSource(corsConfigurationSource()))
                // TODO review and test this
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                        .maximumSessions(1)
                        .maxSessionsPreventsLogin(true))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        // Login and related endpoints
                        // TODO maybe better just use defaults, instead of custom ones
                        .requestMatchers("/auth/**", "/login").permitAll()
                        // Endpoints that return user-specific data (TODO)
                        // .requestMatchers("/playlists").authenticated()
                        // Endpoints that have to be restricted, such as user admin or system configuration
                        .requestMatchers("/users/**").hasAnyAuthority("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/**").hasAnyAuthority("READ")
                        .requestMatchers(HttpMethod.POST, "/**").hasAnyAuthority("CREATE")
                        .requestMatchers(HttpMethod.PATCH, "/**").hasAnyAuthority("UPDATE")
                        .requestMatchers(HttpMethod.PUT, "/**").denyAll()
                        .requestMatchers(HttpMethod.DELETE, "/**").hasAnyAuthority("DELETE")
                        .anyRequest().authenticated()
                )
                .rememberMe(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                // TODO review formLogin and logout
                .formLogin(form -> form
                        .loginPage("/auth/login")
                        .loginProcessingUrl("/auth/login")
                        .defaultSuccessUrl("/", true)
                        .failureUrl("/auth/login?error=true")
                        .permitAll())
                .logout(logout -> logout
                        .logoutUrl("/auth/logout")
                        .logoutSuccessUrl("/auth/login?logout=true")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll())
                .exceptionHandling(c -> c
                        .authenticationEntryPoint(customAuthenticationEntryPoint)
                        .accessDeniedHandler(customAccessDeniedHandler)
                        .accessDeniedPage(null)
                );

        return httpSecurity.build();
    }

    // TODO
    @Bean
    public AuthenticationManager authenticationManager(PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
//        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return new ProviderManager(provider);
    }

    // TODO what is this
    @Bean
    public SecurityContextRepository securityContextRepository() {
        return new HttpSessionSecurityContextRepository();
    }

    // TODO need to understand and configure this
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
