package dev.davidsilva.music.security;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

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
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfiguration {
//    private final TokenProvider tokenProvider;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                // We don't need CSRF because our token is invulnerable
                .csrf(AbstractHttpConfigurer::disable);
        httpSecurity.authorizeHttpRequests(auth -> auth.requestMatchers("/**").permitAll());
        httpSecurity.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        // TODO I don't know if we need to keep the following 3
        httpSecurity.httpBasic(AbstractHttpConfigurer::disable);
        httpSecurity.formLogin(AbstractHttpConfigurer::disable);
        httpSecurity.logout(AbstractHttpConfigurer::disable);
        return httpSecurity.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        // We would add an ignore rule for static resources here, if we had them
        // Also, add ".debug(true)" if debugging info is needed
        return (web) -> web.ignoring().
                requestMatchers(HttpMethod.OPTIONS, "/**");
    }

}
