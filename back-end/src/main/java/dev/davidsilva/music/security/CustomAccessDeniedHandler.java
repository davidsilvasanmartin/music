package dev.davidsilva.music.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.davidsilva.music.audit.AuditLogService;
import dev.davidsilva.music.exception.ApiErrorDto;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    private final ObjectMapper objectMapper;
    private final AuditLogService auditLogService;

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {
        // TODO review logging. Separate table for auth logging ?
        auditLogService.log(
                "ACCESS_DENIED",           // action
                "AUTH",                           // entityType
                request.getRequestURI(),             // entityId1 - request URI
                // request.getRemoteAddr(),             // entityId2 - IP address
                null,                            // userId (not authenticated)
                null,                            // oldValue
                accessDeniedException.getClass().getSimpleName(), // newValue - exception type
                String.format("Access denied: %s. Remote IP: %s, User-Agent: %s",
                        accessDeniedException.getMessage(),
                        request.getRemoteAddr(),
                        request.getHeader("User-Agent")
                )  // description
        );

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.getWriter().write(objectMapper.writeValueAsString(toApiErrorDto(accessDeniedException, request)));
    }

    private ApiErrorDto toApiErrorDto(Exception exception, HttpServletRequest request) {
        String details = String.format(
                "Authorization failed for request to '%s'. " +
                        "Please contact your system administrator for more information.",
                request.getRequestURI(),
                exception.getClass().getSimpleName()
        );

        return new ApiErrorDto(
                new Date(),
                "You do not have permission to access this resource",
                details
        );
    }
}
