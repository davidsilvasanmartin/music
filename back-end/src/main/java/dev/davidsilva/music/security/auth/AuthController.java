package dev.davidsilva.music.security.auth;

import dev.davidsilva.music.security.user.UserDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final SecurityContextRepository securityContextRepository;
    private final AuthService authService;

    // TODO Add @Valid annotation, figure out how to validate requests
    // Check out bean validation for example https://www.baeldung.com/java-bean-validation-not-null-empty-blank
    // TODO just use UsernamePasswordAuthenticationFilter with standard URLs
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthenticationRequest request, HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        authService.authenticate(request, httpRequest, httpResponse);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/user")
    public ResponseEntity<UserDto> user() {
        return ResponseEntity.ok(authService.getLoggedInUser());
    }

}
