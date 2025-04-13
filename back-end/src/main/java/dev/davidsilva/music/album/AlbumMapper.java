package dev.davidsilva.music.album;

import dev.davidsilva.music.artist.ArtistMapper;
import dev.davidsilva.music.genre.Genre;
import dev.davidsilva.music.song.Song;
import dev.davidsilva.music.song.SongDto;
import org.mapstruct.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {ArtistMapper.class})
public interface AlbumMapper {
    // TODO artist eventually needs to become an object; need to update Album, AlbumDto, and frontend, tests
    // TODO genres eventually needs to become objects; need to update Album, AlbumDto, and frontend, tests
    @Named("toAlbumDto")
    @Mapping(target = "id", source = "id")
    @Mapping(target = "albumArtist", source = "artist.name")
    @Mapping(target = "album", source = "album")
    @Mapping(target = "year", source = "year")
    @Mapping(target = "genres", expression = "java(mapGenresToStrings(entity.getGenres()))")
    @Mapping(target = "songs", qualifiedByName = "toSongDtoBasic")
    @BeanMapping(ignoreByDefault = true)
    AlbumDto toAlbumDto(Album entity);

    @IterableMapping(qualifiedByName = "toAlbumDto")
    List<AlbumDto> toAlbumDto(List<Album> entities);
//
//    @Named("toAlbumDtoBasic")
//    @Mapping(target = "id", source = "id")
//    @Mapping(target = "albumArtist", source = "artist.name")
//    @Mapping(target = "album", source = "album")
//    @BeanMapping(ignoreByDefault = true)
//    AlbumDto toAlbumDtoBasic(Album entity);
//
//    // TODO not used, I think
//    @IterableMapping(qualifiedByName = "toAlbumDtoBasic")
//    List<AlbumDto> toAlbumDtoBasic(List<Album> entities);

    // TODO think about front-end executing request to get song instead of this
    @Named("toSongDtoBasic")
    @Mapping(target = "id", source = "id")
    @Mapping(target = "title", source = "title")
//    @Mapping(target = "album", qualifiedByName = "toAlbumDtoBasic")
    @BeanMapping(ignoreByDefault = true)
    SongDto toSongDtoBasic(Song song);

    // TODO not used (example only)
    // Special mapping method to break circular dependency
    @Named("toAlbumDtoWithoutArtist")
    @Mapping(target = "albumArtist", ignore = true)
    @Mapping(target = "genres", expression = "java(mapGenresToStrings(entity.getGenres()))")
    @Mapping(target = "songs", ignore = true)
    AlbumDto toAlbumDtoWithoutArtist(Album entity);

    // TODO not used (example only)
    // Helper method for collections
    @Named("toAlbumDtosWithoutArtist")
    @IterableMapping(qualifiedByName = "toAlbumDtoWithoutArtist")
    List<AlbumDto> toAlbumDtosWithoutArtist(List<Album> entities);

    default List<String> mapGenresToStrings(Set<Genre> genres) {
        if (genres == null) {
            return null;
        }
        return genres.stream()
                .map(Genre::getName)
                .collect(Collectors.toList());
    }
}
