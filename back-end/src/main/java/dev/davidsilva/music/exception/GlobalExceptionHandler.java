package dev.davidsilva.music.exception;

import dev.davidsilva.music.search.InvalidSearchException;
import dev.davidsilva.music.search.InvalidSearchFormatException;
import dev.davidsilva.music.search.InvalidSearchOperationException;
import dev.davidsilva.music.security.auth.AuthExceptionMapper;
import dev.davidsilva.music.song.SongFormatNotSupportedException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.Date;

@ControllerAdvice
@ResponseBody
@RequiredArgsConstructor
public class GlobalExceptionHandler {
    private final AuthExceptionMapper authExceptionMapper;

    @ExceptionHandler(AbstractNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiErrorDto handleNotFoundException(Exception exception, WebRequest webRequest) {
        return toApiErrorDto(exception, webRequest);
    }

    @ExceptionHandler(AbstractValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorDto handleValidationException(Exception exception, WebRequest webRequest) {
        return toApiErrorDto(exception, webRequest);
    }


    @ExceptionHandler({InvalidSearchFormatException.class, InvalidSearchException.class, InvalidSearchOperationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorDto handleInvalidSearchFormatException(Exception exception, WebRequest webRequest) {
        return toApiErrorDto(exception, webRequest);
    }

    @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public ApiErrorDto handleNotAcceptableStatusException(Exception exception, WebRequest webRequest) {
        return toApiErrorDto(exception, webRequest);
    }

    @ExceptionHandler(SongFormatNotSupportedException.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public ApiErrorDto handleSongFormatNotSupportedException(SongFormatNotSupportedException exception, WebRequest webRequest) {
        return toApiErrorDto(exception, webRequest);
    }

    /**
     * Authentication and authorization exceptions can be thrown from components (for example when we try to /login), so
     * they need to be handled here as well as in the handlers we have written for exceptions that are thrown
     * from within filters
     */
    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ApiErrorDto handleAuthenticationException(AuthenticationException exception, HttpServletRequest request) {
        return authExceptionMapper.authenticationExceptionToApiErrorDto(exception, request);
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ApiErrorDto handleAccessDeniedException(AccessDeniedException exception, HttpServletRequest request) {
        return authExceptionMapper.authorizationExceptionToApiErrorDto(exception, request);
    }

    /**
     * This handler requires that we disable serving static resources, because they are registered under a "/**" route
     * (if enabled), meaning Spring would look for static resources for any route that does not correspond to one of
     * the @Controllers. Then, it would throw an exception (because a static resource would not be found), and that
     * exception would not be captured by this @ControllerAdvice
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiErrorDto handleNoHandlerFoundException(Exception exception, WebRequest webRequest) {
        return toApiErrorDto(exception, webRequest);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public ApiErrorDto handleMethodNotAllowedException(Exception exception, WebRequest webRequest) {
        return toApiErrorDto(exception, webRequest);
    }

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiErrorDto handleThrowable(Throwable exception, WebRequest webRequest) {
        return toApiErrorDto(exception, webRequest);
    }

    private ApiErrorDto toApiErrorDto(Throwable throwable, WebRequest webRequest) {
        return new ApiErrorDto(new Date(), throwable.getMessage(), webRequest.getDescription(false));
    }
}
