package dev.davidsilva.music.exception;

import dev.davidsilva.music.album.AlbumNotFoundException;
import dev.davidsilva.music.search.InvalidSearchException;
import dev.davidsilva.music.search.InvalidSearchFormatException;
import dev.davidsilva.music.search.InvalidSearchOperationException;
import dev.davidsilva.music.song.SongNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.NotAcceptableStatusException;

import java.util.Date;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(AlbumNotFoundException.class)
    public ResponseEntity<ApiErrorDto> handleAlbumNotFoundException(AlbumNotFoundException exception, WebRequest webRequest) {
        ApiErrorDto apiErrorDto = new ApiErrorDto(new Date(), exception.getMessage(), webRequest.getDescription(false));
        return new ResponseEntity<>(apiErrorDto, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(SongNotFoundException.class)
    public ResponseEntity<ApiErrorDto> handleSongNotFoundException(SongNotFoundException exception, WebRequest webRequest) {
        ApiErrorDto apiErrorDto = new ApiErrorDto(new Date(), exception.getMessage(), webRequest.getDescription(false));
        return new ResponseEntity<>(apiErrorDto, HttpStatus.NOT_FOUND);
    }

    // TODO: group all search exceptions into one (subclass each particular case)

    @ExceptionHandler(InvalidSearchFormatException.class)
    public ResponseEntity<ApiErrorDto> handleInvalidSearchFormatException(InvalidSearchFormatException exception, WebRequest webRequest) {
        ApiErrorDto apiErrorDto = new ApiErrorDto(new Date(), exception.getMessage(), webRequest.getDescription(false));
        return new ResponseEntity<>(apiErrorDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidSearchException.class)
    public ResponseEntity<ApiErrorDto> handleInvalidSearchException(InvalidSearchException exception, WebRequest webRequest) {
        ApiErrorDto apiErrorDto = new ApiErrorDto(new Date(), exception.getMessage(), webRequest.getDescription(false));
        return new ResponseEntity<>(apiErrorDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidSearchOperationException.class)
    public ResponseEntity<ApiErrorDto> handleInvalidSearchOperationException(InvalidSearchOperationException exception, WebRequest webRequest) {
        ApiErrorDto apiErrorDto = new ApiErrorDto(new Date(), exception.getMessage(), webRequest.getDescription(false));
        return new ResponseEntity<>(apiErrorDto, HttpStatus.BAD_REQUEST);
    }

    // TODO: this is not working at the moment
    @ExceptionHandler(NotAcceptableStatusException.class)
    public ResponseEntity<ApiErrorDto> handleNotAcceptableStatusException(NotAcceptableStatusException exception, WebRequest webRequest) {
        ApiErrorDto apiErrorDto = new ApiErrorDto(new Date(), exception.getMessage(), webRequest.getDescription(false));
        return new ResponseEntity<>(apiErrorDto, HttpStatus.NOT_ACCEPTABLE);
    }

//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<ApiErrorDto> handleGenericException(Exception exception, WebRequest webRequest) {
//        ApiErrorDto apiErrorDto = new ApiErrorDto(new Date(), exception.getMessage(), webRequest.getDescription(false));
//        return new ResponseEntity<>(apiErrorDto, HttpStatus.INTERNAL_SERVER_ERROR);
//    }
}
