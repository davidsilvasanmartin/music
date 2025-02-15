package dev.davidsilva.music.security.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("users")
public class UserController {
    private final UserService userService;

    @GetMapping("/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable String username) {
        Optional<User> userOptional = userService.findByUsername(username);
        return userOptional
                .map(ResponseEntity::ok)
                // TODO test this not found, maybe create custom exception
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User createdUser = userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @PutMapping("/{username}/roles/{roleName}")
    public ResponseEntity<Void> addRoleToUser(@PathVariable String username, @PathVariable String roleName) {
        try {
            userService.addRoleToUser(username, roleName);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            // TODO test this not found, maybe create custom exception
            return ResponseEntity.notFound().build();
        }
    }

    public String test1OnlyForDemoPlsRemove() {
        // One can get hold of the context by using SecurityContextHolder...
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication a = context.getAuthentication();
        return "Hello, " + a.getName() + "!";
    }

    public String test2OnlyForDemoPlsRemove(Authentication a) {
        // ... Or we can inject it into our method thanks to Spring
        return "Hello, " + a.getName() + "!";
    }
}