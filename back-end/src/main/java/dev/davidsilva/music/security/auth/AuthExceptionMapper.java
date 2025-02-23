package dev.davidsilva.music.security.auth;

import dev.davidsilva.music.exception.ApiErrorDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class AuthExceptionMapper {
    public ApiErrorDto authorizationExceptionToApiErrorDto(AccessDeniedException exception, HttpServletRequest request) {
        String details = String.format(
                "Authorization failed for request to '%s'. " +
                        "Please contact your system administrator for more information.",
                request.getRequestURI()
        );

        return new ApiErrorDto(
                new Date(),
                "You do not have permission to access this resource.",
                details
        );
    }

    public ApiErrorDto authenticationExceptionToApiErrorDto(AuthenticationException exception, HttpServletRequest request) {
        String details = String.format(
                "Authentication failed for request to '%s'. " +
                        "Please ensure you are properly authenticated to access this resource.",
                request.getRequestURI()
        );

        return new ApiErrorDto(
                new Date(),
                "Authentication failed. Full authentication is required to access this resource.",
                details
        );
    }
}
