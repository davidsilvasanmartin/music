package dev.davidsilva.music.security;

import dev.davidsilva.music.security.auth.CustomAccessDeniedHandler;
import dev.davidsilva.music.security.auth.CustomAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * See the following links:
 * https://www.baeldung.com/spring-security-httpsecurity-vs-websecurity
 * https://spring.io/blog/2022/02/21/spring-security-without-the-websecurityconfigureradapter
 * https://docs.spring.io/spring-security/reference/5.8/migration/servlet/config.html
 */

/**
 * TODO watch this https://www.youtube.com/watch?v=96vK5BDpT7g
 */

@Configuration
//@EnableWebSecurity
@EnableWebSecurity(debug = true)
@RequiredArgsConstructor
public class SecurityConfiguration {
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    /**
     * TODO somewhat we are still redirecting to /error when there is any error (TODO find a way to test this and fix).
     * One way to force this error is to comment the permitAll() for GET requests below and trying to GET /albums with
     * a valid JWT token of a valid authenticated user
     * <p>
     * TODO server is sending header: WWW-Authenticate: Basic realm="Secure Area"
     * <p>
     * TODO get rid of Redis, use in-memory normal sessions. Read this to learn https://stackoverflow.com/questions/3106452/how-do-servlets-work-instantiation-sessions-shared-variables-and-multithreadi/3106909#3106909
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                // TODO enable csrf
                .csrf(AbstractHttpConfigurer::disable)
                .cors(c -> c.configurationSource(corsConfigurationSource()))
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                        // TODO get from properties maybe. And test. Right now I'm not sure what it does
                        .maximumSessions(1)
                        .maxSessionsPreventsLogin(true)
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        // The auth endpoints that should always be allowed are defined here
                        .requestMatchers("/auth/login").permitAll()
                        // Endpoints that return user-specific data (TODO)
                        // .requestMatchers("/playlists").authenticated()
                        // Endpoints that have to be restricted, such as user admin or system configuration
                        .requestMatchers("/users/**").hasAnyAuthority("ADMIN")
                        .requestMatchers("/jobs/**").permitAll() // TODO allow only for admins
                        .requestMatchers(HttpMethod.GET, "/**").hasAnyAuthority("READ")
                        .requestMatchers(HttpMethod.POST, "/**").hasAnyAuthority("CREATE")
                        .requestMatchers(HttpMethod.PATCH, "/**").hasAnyAuthority("UPDATE")
                        .requestMatchers(HttpMethod.PUT, "/**").denyAll()
                        .requestMatchers(HttpMethod.DELETE, "/**").hasAnyAuthority("DELETE")
                        .anyRequest().authenticated()
                )
                .rememberMe(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                // TODO review logout
                .logout(logout -> logout
                        .logoutUrl("/auth/logout")
                        // TODO This was provided by AI
                        // .logoutSuccessUrl("/auth/login?logout=true")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        // TODO this is in Github project
                        // .addLogoutHandler(new CustomLogoutHandler(this.redisIndexedSessionRepository))
                        // .logoutSuccessHandler((request, response, authentication) ->
                        //        SecurityContextHolder.clearContext()
                        // )
                        .permitAll())
                .exceptionHandling(c -> c
                        .authenticationEntryPoint(customAuthenticationEntryPoint)
                        .accessDeniedHandler(customAccessDeniedHandler)
                        .accessDeniedPage(null)
                );

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
