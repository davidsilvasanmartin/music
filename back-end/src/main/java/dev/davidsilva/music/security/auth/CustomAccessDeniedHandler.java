package dev.davidsilva.music.security.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.davidsilva.music.audit.AuditLogService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    private final ObjectMapper objectMapper;
    private final AuditLogService auditLogService;
    private final AuthExceptionMapper authExceptionMapper;

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
        response.getWriter().write(objectMapper.writeValueAsString(
                authExceptionMapper.authorizationExceptionToApiErrorDto(accessDeniedException, request))
        );
    }
}
