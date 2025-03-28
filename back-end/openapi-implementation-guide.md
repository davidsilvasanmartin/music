# OpenAPI 3.1 and Swagger Implementation Guide

## Introduction

This document outlines the proposed changes for implementing OpenAPI 3.1 and Swagger documentation for the Music API.
OpenAPI is a specification for machine-readable interface files for describing, producing, consuming, and visualizing
RESTful web services. Swagger is a set of tools for implementing the OpenAPI specification.

## Proposed Changes

### 1. Add Dependencies

Add the following dependencies to the `pom.xml` file:

```xml
<!-- SpringDoc OpenAPI UI -->
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.8.5</version>
</dependency>
```

### 2. Create OpenAPI Configuration Class

Create a new configuration class to customize the OpenAPI documentation:

```java
package dev.davidsilva.music.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Music API")
                        .description("API for managing music, albums, songs, and playlists")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("David Silva")
                                .email("contact@davidsilva.dev")
                                .url("https://davidsilva.dev"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server()
                                .url("/")
                                .description("Default Server URL")))
                .components(new Components()
                        .addSecuritySchemes("cookieAuth", new SecurityScheme()
                                .type(SecurityScheme.Type.APIKEY)
                                .in(SecurityScheme.In.COOKIE)
                                .name("JSESSIONID")))
                .addSecurityItem(new SecurityRequirement().addList("cookieAuth"));
    }
}
```

### 3. Configure OpenAPI Properties

Add the following properties to `application.properties` or `application.yml`:

```properties
# OpenAPI Documentation
springdoc.api-docs.path=/api-docs
springdoc.swagger-ui.path=/swagger-ui.html
springdoc.swagger-ui.operationsSorter=method
springdoc.swagger-ui.tagsSorter=alpha
springdoc.swagger-ui.tryItOutEnabled=true
springdoc.swagger-ui.filter=true
springdoc.swagger-ui.syntaxHighlight.activated=true
```

### 4. Document Controllers with OpenAPI Annotations

#### Example: AlbumController

```java
package dev.davidsilva.music.album;

import dev.davidsilva.music.search.SearchStringMapper;
import dev.davidsilva.music.utils.PaginatedResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

@RestController
@RequiredArgsConstructor
@CrossOrigin
@RequestMapping("albums")
@Tag(name = "Album", description = "Album management APIs")
public class AlbumController {
    private final AlbumService albumService;
    private final SearchStringMapper searchStringMapper;

    @Operation(summary = "Get all albums", description = "Returns a paginated list of albums with optional search")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved albums",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PaginatedResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @GetMapping
    public ResponseEntity<PaginatedResponse<AlbumDto>> getAlbums(
            @Parameter(description = "Pagination and sorting parameters")
            @PageableDefault(page = 0, size = 10)
            @SortDefault.SortDefaults({
                    @SortDefault(sort = "albumArtist", direction = Sort.Direction.ASC),
                    @SortDefault(sort = "year", direction = Sort.Direction.ASC)
            })
            Pageable pageable,
            @Parameter(description = "Search string for filtering albums")
            @RequestParam(value = "search", required = false) String search
    ) {
        // Implementation remains the same
    }

    @Operation(summary = "Get album by ID", description = "Returns an album by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved album",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AlbumDto.class))),
            @ApiResponse(responseCode = "404", description = "Album not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @GetMapping("/{id}")
    public ResponseEntity<AlbumDto> getAlbumById(
            @Parameter(description = "Album ID", required = true)
            @PathVariable("id") int id
    ) {
        // Implementation remains the same
    }

    @Operation(summary = "Get album artwork", description = "Returns the album artwork image")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved album artwork",
                    content = @Content(mediaType = "image/*")),
            @ApiResponse(responseCode = "404", description = "Album artwork not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @GetMapping(value = "{id}/albumArt", produces = {MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_GIF_VALUE})
    @ResponseBody
    public ResponseEntity<FileSystemResource> getAlbumArtById(
            @Parameter(description = "Album ID", required = true)
            @PathVariable("id") int id
    ) {
        // Implementation remains the same
    }
}
```

#### Example: Document Models with OpenAPI Annotations

```java
package dev.davidsilva.music.album;

import com.fasterxml.jackson.annotation.JsonInclude;
import dev.davidsilva.music.song.SongDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
@Data
@Schema(description = "Album information")
public class AlbumDto {
    @Schema(description = "Album ID", example = "1")
    private int id;

    @Schema(description = "Album artist name", example = "The Beatles")
    private String albumArtist;

    @Schema(description = "Album title", example = "Abbey Road")
    private String album;

    @Schema(description = "Album genres", example = "[\"Rock\", \"Pop\"]")
    private List<String> genres;

    @Schema(description = "Album release year", example = "1969")
    private int year;

    @Schema(description = "Songs in the album")
    private List<SongDto> songs;
}
```

### 5. Document Authentication and Security

#### Example: AuthController

```java
package dev.davidsilva.music.security.auth;

import dev.davidsilva.music.security.user.UserDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Authentication", description = "Authentication APIs")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final SecurityContextRepository securityContextRepository;
    private final AuthService authService;

    @Operation(summary = "Login", description = "Authenticate a user and return user information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully authenticated",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDto.class))),
            @ApiResponse(responseCode = "401", description = "Authentication failed")
    })
    @PostMapping("/login")
    public ResponseEntity<?> login(
            @Parameter(description = "Authentication request with username and password", required = true)
            @RequestBody AuthenticationRequest request,
            HttpServletRequest httpRequest,
            HttpServletResponse httpResponse
    ) {
        // Implementation remains the same
    }

    @Operation(summary = "Get current user", description = "Get information about the currently logged-in user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved user information",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDto.class))),
            @ApiResponse(responseCode = "401", description = "Not authenticated")
    })
    @GetMapping("/user")
    public ResponseEntity<UserDto> user() {
        // Implementation remains the same
    }
}
```

### 6. Document Authentication Request Model

```java
package dev.davidsilva.music.security.auth;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Authentication request")
public record AuthenticationRequest(
        @Schema(description = "Username", example = "admin", required = true)
        String username,

        @Schema(description = "Password", example = "password", required = true)
        String password
) {
}
```

## Benefits of OpenAPI Documentation

1. **Interactive API Documentation**: Swagger UI provides an interactive interface for exploring and testing the API.
2. **Client Code Generation**: OpenAPI specifications can be used to generate client code in various languages.
3. **API Standardization**: Enforces a standard way of documenting APIs.
4. **Improved Developer Experience**: Makes it easier for developers to understand and use the API.
5. **API Governance**: Helps maintain API quality and consistency.

## Accessing the Documentation

After implementing these changes, the OpenAPI documentation will be available at:

- Swagger UI: `/swagger-ui.html`
- OpenAPI JSON: `/api-docs`
- OpenAPI YAML: `/api-docs.yaml`

## Conclusion

Implementing OpenAPI 3.1 and Swagger documentation will significantly improve the usability and maintainability of the
Music API. The proposed changes provide a comprehensive approach to documenting the API endpoints, request/response
models, and security requirements.