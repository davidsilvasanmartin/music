package dev.davidsilva.music.security.user;

import dev.davidsilva.music.search.SearchStringMapper;
import dev.davidsilva.music.utils.PaginatedResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * TODO DTOs !!!
 */

@RestController
@RequiredArgsConstructor
@RequestMapping("users")
public class UserController {
    private final UserService userService;
    private final SearchStringMapper searchStringMapper;

    @GetMapping
    public ResponseEntity<PaginatedResponse<UserDto>> getUsers(
            @SortDefault.SortDefaults(@SortDefault(sort = "updatedAt", direction = Sort.Direction.DESC))
            Pageable pageable,
            @RequestParam(value = "search", required = false) String search
    ) {
        PaginatedResponse<UserDto> paginatedUsers;
        if (search != null) {
            UserSpecification specification = new UserSpecification(searchStringMapper.toSearchCriteria(search));
            paginatedUsers = userService.findAll(specification, pageable);
        } else {
            paginatedUsers = userService.findAll(pageable);
        }
        return new ResponseEntity<>(paginatedUsers, HttpStatus.OK);
    }

    // TODO use DTO because now we are sending password to frontend !!!
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
}