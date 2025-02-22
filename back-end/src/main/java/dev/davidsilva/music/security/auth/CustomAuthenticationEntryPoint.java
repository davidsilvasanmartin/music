package dev.davidsilva.music.security.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.davidsilva.music.audit.AuditLogService;
import dev.davidsilva.music.exception.ApiErrorDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Base64;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final ObjectMapper objectMapper;
    private final AuditLogService auditLogService;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        String authHeader = request.getHeader("Authorization");
        String username = extractUsername(authHeader);

        String action = (authException instanceof InsufficientAuthenticationException)
                ? "MISSING_AUTHENTICATION"
                : "AUTHENTICATION_FAILED";
        String description;
        if (username != null) {
            description = String.format("Authentication failed for user '%s': %s. Remote IP: %s, User-Agent: %s",
                    username,
                    authException.getMessage(),
                    request.getRemoteAddr(),
                    request.getHeader("User-Agent"));
        } else {
            description = String.format("No credentials provided. Remote IP: %s, User-Agent: %s",
                    request.getRemoteAddr(),
                    request.getHeader("User-Agent"));
        }

        // TODO review logging. Separate table for auth logging ?
        auditLogService.log(
                action,
                "AUTH",                           // entityType
                request.getRequestURI(),             // entityId1 - request URI
                //request.getRemoteAddr(),             // entityId2 - IP address
                null,                            // userId (not authenticated)
                null,                            // oldValue
                authException.getClass().getSimpleName(), // newValue - exception type
                description
        );

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        // TODO this is only for HTTP Basic
        response.setHeader("WWW-Authenticate", "Basic realm=\"Secure Area\"");
        response.getWriter().write(objectMapper.writeValueAsString(toApiErrorDto(authException, request)));
    }

    private ApiErrorDto toApiErrorDto(Exception exception, HttpServletRequest request) {
        String details = String.format(
                "Authentication failed for request to '%s'. " +
                        "Please ensure you are properly authenticated to access this resource.",
                request.getRequestURI()
        );

        return new ApiErrorDto(
                new Date(),
                "Authentication failed. Full authentication is required to access this resource",
                details
        );
    }


    // TODO only valid for HTTP Basic
    private String extractUsername(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Basic ")) {
            try {
                String base64Credentials = authHeader.substring("Basic ".length());
                String credentials = new String(Base64.getDecoder().decode(base64Credentials));
                return credentials.split(":", 2)[0];
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }

}
