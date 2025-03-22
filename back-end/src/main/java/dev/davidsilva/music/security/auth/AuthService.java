package dev.davidsilva.music.security.auth;

import dev.davidsilva.music.security.user.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    // TODO bring from properties file
    private final int maxSessions = 10;
    private final AuthenticationManager authenticationManager;
    private final SecurityContextRepository securityContextRepository;
    //    private final SessionRegistry sessionRegistry;
//    private final RedisIndexedSessionRepository redisIndexedSessionRepository;
    private final UserService userService;
    private final UserRepository userRepository;
    private final UserDtoMapper userDtoMapper;

    public void authenticate(AuthenticationRequest request, HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        Authentication authenticationRequest =
                UsernamePasswordAuthenticationToken.unauthenticated(request.username(), request.password());
        Authentication authenticationResponse = authenticationManager.authenticate(authenticationRequest);

        // TODO does this do something to the session ??
        var context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authenticationResponse);
        SecurityContextHolder.setContext(context);

        // In Spring Security 6, the default procedure is that we manually save the Authentication in the HttpSession
        // using the SecurityContextRepository. See:
        // https://docs.spring.io/spring-security/reference/servlet/authentication/session-management.html#requireexplicitsave
        securityContextRepository.saveContext(context, httpRequest, httpResponse);
    }

    public UserDto getLoggedInUserDto() {
        DbUserDetails userDetails = (DbUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userService.findByUsername(userDetails.getUsername());
    }

    // TODO not sure if we could write a method in UserService for this, to remove dependency on UserRepository
    public User getLoggedInUser() {
        DbUserDetails userDetails = (DbUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userRepository.findByUsername(userDetails.getUsername()).orElseThrow(() -> new UserNotFoundException(userDetails.getUsername()));
    }

    /**
     * TODO check what UsernamePasswordAuthenticationFilter does, and copy
     * Method is responsible for validating user session is not exceeded. If it has been exceeded, the oldest valid
     * session is removed/invalidated
     *
     * @param authentication The authentication
     */
    private void validateMaxSession(Authentication authentication) {
        // If max session is negative means unlimited session
        if (maxSessions <= 0) {
            // TODO return; here
        }

//        var principal = (UserDetails) authentication.getPrincipal();
//        List<SessionInformation> sessions = this.sessionRegistry.getAllSessions(principal, false);

//        if (sessions.size() >= maxSessions) {
//            sessions.stream() //
//                    // Gets the oldest session
//                    .min(Comparator.comparing(SessionInformation::getLastRequest)) //
//                    .ifPresent(sessionInfo -> this.redisIndexedSessionRepository.deleteById(sessionInfo.getSessionId()));
//        }
    }
}
