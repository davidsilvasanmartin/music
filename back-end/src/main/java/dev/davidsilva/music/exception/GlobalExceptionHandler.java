package dev.davidsilva.music.exception;

import dev.davidsilva.music.search.InvalidSearchException;
import dev.davidsilva.music.search.InvalidSearchFormatException;
import dev.davidsilva.music.search.InvalidSearchOperationException;
import dev.davidsilva.music.song.SongFormatNotSupportedException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.NotAcceptableStatusException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.naming.AuthenticationException;
import java.util.Date;

@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {
    @ExceptionHandler(AbstractNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiErrorDto handleAlbumNotFoundException(Exception exception, WebRequest webRequest) {
        return toApiErrorDto(exception, webRequest);
    }


    @ExceptionHandler({InvalidSearchFormatException.class, InvalidSearchException.class, InvalidSearchOperationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorDto handleInvalidSearchFormatException(Exception exception, WebRequest webRequest) {
        return toApiErrorDto(exception, webRequest);
    }

    // TODO: this is not working at the moment
    @ExceptionHandler(NotAcceptableStatusException.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public ApiErrorDto handleNotAcceptableStatusException(NotAcceptableStatusException exception, WebRequest webRequest) {
        return toApiErrorDto(exception, webRequest);
    }

    @ExceptionHandler(SongFormatNotSupportedException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiErrorDto handleInvalidSearchOperationException(SongFormatNotSupportedException exception, WebRequest webRequest) {
        return toApiErrorDto(exception, webRequest);
    }

    // TODO not working because these are thrown in filters
    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ApiErrorDto handleAuthenticationException(AuthenticationException exception, WebRequest webRequest) {
        return toApiErrorDto(exception, webRequest);
    }

    // TODO not working because these are thrown in filters
    @ExceptionHandler(AccessDeniedException.class)
//    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseStatus(HttpStatus.I_AM_A_TEAPOT)
    public ApiErrorDto handleAccessDeniedException(AccessDeniedException exception, WebRequest webRequest) {
        return toApiErrorDto(exception, webRequest);
    }

    /**
     * This handler requires that we disable serving static resources, because they are registered under a "/**" route
     * (if enabled), meaning Spring would look for static resources for any route that does not correspond to one of
     * the @Controllers. Then, it would throw an exception (because a static resource would not be found), and that
     * exception would not be captured by this @ControllerAdvice
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiErrorDto handleNoHandlerFoundException(NoHandlerFoundException exception, WebRequest webRequest) {
        return toApiErrorDto(exception, webRequest);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public ApiErrorDto handleMethodNotAllowedException(HttpRequestMethodNotSupportedException exception, WebRequest webRequest) {
        return toApiErrorDto(exception, webRequest);
    }

    private ApiErrorDto toApiErrorDto(Exception exception, WebRequest webRequest) {
        return new ApiErrorDto(new Date(), exception.getMessage(), webRequest.getDescription(false));
    }
}
