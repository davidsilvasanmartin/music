package dev.davidsilva.music.exception;

import dev.davidsilva.music.search.InvalidSearchException;
import dev.davidsilva.music.search.InvalidSearchFormatException;
import dev.davidsilva.music.search.InvalidSearchOperationException;
import dev.davidsilva.music.song.SongFormatNotSupportedException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.NotAcceptableStatusException;

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

    private ApiErrorDto toApiErrorDto(Exception exception, WebRequest webRequest) {
        return new ApiErrorDto(new Date(), exception.getMessage(), webRequest.getDescription(false));
    }
}
