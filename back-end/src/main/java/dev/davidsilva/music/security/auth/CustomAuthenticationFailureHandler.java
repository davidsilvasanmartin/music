package dev.davidsilva.music.security.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.davidsilva.music.audit.AuditLogService;
import dev.davidsilva.music.exception.ApiErrorDto;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;

// TODO not used
@Component
@RequiredArgsConstructor
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {
    private final ObjectMapper objectMapper;
    private final AuditLogService auditLogService;

    @Override
    public void onAuthenticationFailure(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authenticationException) throws IOException, ServletException {

        String username = request.getParameter("username"); // or however you receive the username

        auditLogService.log(
                "AUTHENTICATION_FAILED",
                "AUTH",
                username,                         // entityId1 - attempted username
                // request.getRemoteAddr(),          // entityId2 - IP address
                null,                            // userId (not authenticated)
                null,                            // oldValue
                authenticationException.getClass().getSimpleName(), // newValue - exception type
                String.format("Login failed for user '%s': %s. Remote IP: %s, User-Agent: %s",
                        username,
                        authenticationException.getMessage(),
                        request.getRemoteAddr(),
                        request.getHeader("User-Agent")
                )
        );


        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(objectMapper.writeValueAsString(toApiErrorDto(authenticationException, request)));
    }

    private ApiErrorDto toApiErrorDto(Exception exception, HttpServletRequest request) {
        String details = String.format(
                "Authentication failed for request to '%s'. Error type: %s. " +
                        "Please ensure you are properly authenticated to access this resource.",
                request.getRequestURI(),
                exception.getClass().getSimpleName()
        );

        return new ApiErrorDto(
                new Date(),
                exception.getMessage() != null ? exception.getMessage() : "Authentication Failed",
                details
        );
    }
}
