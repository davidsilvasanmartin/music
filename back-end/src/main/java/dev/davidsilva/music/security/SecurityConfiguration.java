package dev.davidsilva.music.security;

import dev.davidsilva.music.security.auth.CustomAccessDeniedHandler;
import dev.davidsilva.music.security.auth.CustomAuthenticationEntryPoint;
import dev.davidsilva.music.security.auth.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
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
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    /**
     * TODO somewhat we are still redirecting to /error when there is any error (TODO find a way to test this and fix).
     * One way to force this error is to comment the permitAll() for GET requests below and trying to GET /albums with
     * a valid JWT token of a valid authenticated user
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                // We don't need CSRF because our token is invulnerable
                .csrf(AbstractHttpConfigurer::disable)
                // TODO need to find out about CORS
                .cors(c -> c.configurationSource(corsConfigurationSource()))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // TODO review this
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        // Login and related endpoints
                        .requestMatchers("/auth/**").permitAll()
                        // Endpoints that return user-specific data (TODO)
                        .requestMatchers("/playlists").authenticated()
                        // Endpoints that have to be restricted, such as user admin or system configuration
                        .requestMatchers("/users/**").hasAnyAuthority("ADMIN")
                        // We allow getting data such as list of songs and albums (TODO review)
                        .requestMatchers(HttpMethod.GET, "/**").permitAll()
                        .anyRequest().denyAll()
                )
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(c -> c
                        .authenticationEntryPoint(customAuthenticationEntryPoint)
                        .accessDeniedHandler(customAccessDeniedHandler)
                        .accessDeniedPage(null)
                ).cors(c -> c.configurationSource(corsConfigurationSource()));

        return httpSecurity.build();
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
