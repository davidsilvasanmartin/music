package dev.davidsilva.music.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.davidsilva.music.search.InvalidSearchException;
import dev.davidsilva.music.search.InvalidSearchFormatException;
import dev.davidsilva.music.search.InvalidSearchOperationException;
import dev.davidsilva.music.song.SongFormatNotSupportedException;
import jakarta.annotation.Nonnull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.NotAcceptableStatusException;

import java.io.IOException;
import java.util.Date;

/**
 * This filter is needed because I would like to use my own DTO class for all error responses. @ControllerAdvice only
 * handles exceptions thrown by controllers, but I want to catch the ones thrown by Spring Security filters as well
 */
// TODO not working well, disabling
//@Component
//@Order(Ordered.HIGHEST_PRECEDENCE)
public class CustomExceptionTranslationFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(@Nonnull HttpServletRequest request, @Nonnull HttpServletResponse response, @Nonnull FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (AbstractNotFoundException e) {
            writeResponse(HttpStatus.NOT_FOUND, e, response);
        } catch (InvalidSearchFormatException | InvalidSearchException | InvalidSearchOperationException e) {
            writeResponse(HttpStatus.BAD_REQUEST, e, response);
        } catch (NotAcceptableStatusException e) {
            writeResponse(HttpStatus.NOT_ACCEPTABLE, e, response);
            // TODO make some abstract class for this type of exceptions ?
        } catch (SongFormatNotSupportedException e) {
            writeResponse(HttpStatus.INTERNAL_SERVER_ERROR, e, response);
        } catch (AuthenticationException e) {
            writeResponse(HttpStatus.UNAUTHORIZED, e, response);
        } catch (AccessDeniedException e) {
            writeResponse(HttpStatus.FORBIDDEN, e, response);
        } catch (Exception e) {
            writeResponse(HttpStatus.INTERNAL_SERVER_ERROR, e, response);
        }
    }

    private void writeResponse(HttpStatus httpStatus, Exception exception, HttpServletResponse response) throws IOException {
        ApiErrorDto apiErrorDto = new ApiErrorDto(new Date(), exception.getMessage(), null);
        response.setStatus(httpStatus.value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonResponse = objectMapper.writeValueAsString(apiErrorDto);
            response.getWriter().write(jsonResponse);
        } catch (IOException e) {
            // This throws another IOException, not sure what's the best thing to do here
            response.getWriter().write("{\"error\":\"An error occurred while processing the error response.\"}");
        }
        response.flushBuffer();
    }
}
