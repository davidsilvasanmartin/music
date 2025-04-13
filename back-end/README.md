Back-end

# Decisions, notes about this project, in-depth explanations

Here there are in-depth explanations for several topics

- [Spring Batch](docs/spring-batch.md)
- [Spring/JPA Repositories](docs/repositories.md)

# TODO

## Mapping, DTOs, etc

- Need to check out `MapStruct`.

MapStruct could indeed be advantageous for your project. Let me break down the pros and cons to help you decide:

### Advantages of Using MapStruct

1. **Eliminates Boilerplate Code**: MapStruct generates all the mapping code at compile time, reducing the manual
   mapping code you've been writing.
2. **Type Safety**: Compile-time checking ensures your mappings are type-safe, catching issues before runtime.
3. **Performance**: MapStruct generates plain method calls rather than using reflection, making it more efficient than
   other mapping frameworks.
4. **Automatic Handling of Nested Objects**: MapStruct intelligently handles nested mappings, which would solve your
   circular dependency issue elegantly.
5. **Maintainability**: When you need to update your DTOs or entities with new fields, you only need to update the
   mapper interfaces, not implement all the mapping logic.
6. **Custom Mapping Support**: For complex cases, MapStruct allows custom mapping methods and expressions.
7. **Consistent Mapping Patterns**: Enforces a consistent approach to mapping across your application.

### Example of MapStruct Implementation for Your Project

```java
// Add these dependencies to your pom.xml
// <dependency>
//     <groupId>org.mapstruct</groupId>
//     <artifactId>mapstruct</artifactId>
//     <version>1.5.5.Final</version>
// </dependency>
// <dependency>
//     <groupId>org.mapstruct</groupId>
//     <artifactId>mapstruct-processor</artifactId>
//     <version>1.5.5.Final</version>
//     <scope>provided</scope>
// </dependency>

package dev.davidsilva.music.mapper;

import dev.davidsilva.music.album.Album;
import dev.davidsilva.music.album.AlbumDto;
import dev.davidsilva.music.song.Song;
import dev.davidsilva.music.song.SongDto;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {})
public interface MusicMapper {

    @Mapping(target = "songs", ignore = true)
    @Mapping(target = "albumArtist", source = "artist.name")
    AlbumDto albumToDtoWithoutSongs(Album album);

    @Mapping(target = "albumArtist", source = "artist.name")
    AlbumDto albumToDto(Album album);

    @Mapping(target = "album", ignore = true)
    SongDto songToDtoWithoutAlbum(Song song);

    @Mapping(target = "album", expression = "java(albumToDtoWithoutSongs(song.getAlbum()))")
    SongDto songToDto(Song song);

    // After mapping songs to AlbumDto, set up the songs
    @AfterMapping
    default void setSongsInAlbumDto(Album album, @MappingTarget AlbumDto albumDto) {
        if (album.getSongs() != null) {
            albumDto.setSongs(album.getSongs().stream()
                    .map(this::songToDtoWithoutAlbum)
                    .toList());
        }
    }

    // Entity mapping methods (when needed)
    Album albumDtoToEntity(AlbumDto albumDto);

    Song songDtoToEntity(SongDto songDto);
}
```

### Considerations Before Adopting MapStruct

1. **Learning Curve**: There's a small learning curve to understand MapStruct annotations and configuration.
2. **Build Process Integration**: You need to ensure your build process correctly processes the MapStruct annotations.
3. **IDE Configuration**: To see generated code and get proper IDE support, you might need to configure your IDE.
4. **Project Size Consideration**: For very small projects, the overhead of adding MapStruct might not be justified, but
   your project seems to have enough entity-DTO mappings to benefit.

### Recommendation

Given your current manual mapping implementation and the circular dependency issues you've encountered, I would
recommend adopting MapStruct for your project. It would:

1. Solve the circular dependency problem elegantly
2. Reduce the amount of code you need to maintain
3. Make adding new fields to your entities and DTOs easier
4. Improve performance of your mappings
5. Make your code more maintainable and less error-prone

The initial setup investment will quickly pay off with cleaner code and fewer mapping-related bugs, especially as your
project grows in complexity.

## OAuth2

- Resources to check eventually
    - https://www.youtube.com/watch?v=7zm3mxaAFWk
- Look at how Immich has done Oauth2 (they're a client only I believe):
    - https://github.com/immich-app/immich/issues/33
    - https://github.com/immich-app/immich/pull/884
- On OAuth2 done wrong:
    - http://blog.intothesymmetry.com/2015/06/on-oauth-token-hijacks-for-fun-and.html

# Info, resources, tutorials, etc.

- Any videos from these people are good
    - [Dan Vega](https://www.youtube.com/@DanVega/videos)
    - Gabriel Moiroux
    - Laurentiu Splica
    - Vlad Mihalcea
    - Thorbenn Jansen
    - [Coffee + Software](https://www.youtube.com/@coffeesoftware/videos)

## Spring Security

- Tip: the order of Spring Security filters can be found in the class
  ` org.springframework.security.config.annotation.web.builders.FilterOrderRegistration`.
- Trying to follow this guide at the moment https://spring.io/guides/tutorials/spring-security-and-angular-js

## Sessions

- For Spring Session with Redis, see: https://docs.spring.io/spring-session/reference/getting-started/using-redis.html
- Official docs for session management
    - https://docs.spring.io/spring-security/reference/servlet/authentication/persistence.html
    - https://docs.spring.io/spring-security/reference/servlet/authentication/session-management.html

## JPA/Hibernate

- Read "Java Persistence with Hibernate", published by Manning. Maybe there is a new edition, "Java Persistence with
  Spring Data".
