package dev.davidsilva.music.album;

import dev.davidsilva.music.artist.Artist;
import dev.davidsilva.music.artist.ArtistDto;
import dev.davidsilva.music.genre.Genre;
import dev.davidsilva.music.genre.GenreDto;
import dev.davidsilva.music.song.Song;
import dev.davidsilva.music.song.SongDto;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AlbumMapper {
    @Named("toAlbumDto")
    @Mapping(target = "id", source = "id")
    @Mapping(target = "artist", qualifiedByName = "toArtistDtoBasic")
    @Mapping(target = "album", source = "album")
    @Mapping(target = "year", source = "year")
    @Mapping(target = "genres", qualifiedByName = "toGenreDtoBasic")
    @Mapping(target = "songs", qualifiedByName = "toSongDtoBasic")
    @BeanMapping(ignoreByDefault = true)
    AlbumDto toAlbumDto(Album entity);

    @IterableMapping(qualifiedByName = "toAlbumDto")
    List<AlbumDto> toAlbumDto(List<Album> entities);

    @Named("toSongDtoBasic")
    @Mapping(target = "id", source = "id")
    @Mapping(target = "title", source = "title")
    @BeanMapping(ignoreByDefault = true)
    SongDto toSongDtoBasic(Song song);

    @Named("toArtistDtoBasic")
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @BeanMapping(ignoreByDefault = true)
    ArtistDto toArtistDto(Artist artist);

    @Named("toGenreDtoBasic")
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @BeanMapping(ignoreByDefault = true)
    GenreDto toGenreDto(Genre genre);
}
