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

    @GetMapping("/{username}")
    public ResponseEntity<UserDto> getUserByUsername(@PathVariable String username) {
        UserDto user = userService.findByUsername(username);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto user) {
        UserDto newUser = userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<Void> deleteUser(@PathVariable String username) {
        userService.deleteByUsername(username);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{username}/roles/{roleName}")
    public ResponseEntity<Void> addRoleToUser(@PathVariable String username, @PathVariable String roleName) {
        userService.addRoleToUser(username, roleName);
        return ResponseEntity.ok().build();
    }
}