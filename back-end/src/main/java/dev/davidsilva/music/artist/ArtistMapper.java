package dev.davidsilva.music.artist;

import dev.davidsilva.music.album.Album;
import dev.davidsilva.music.album.AlbumDto;
import dev.davidsilva.music.genre.Genre;
import dev.davidsilva.music.genre.GenreDto;
import dev.davidsilva.music.song.Song;
import dev.davidsilva.music.song.SongDto;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ArtistMapper {
    @Named("toArtistDto")
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "mbArtistId", source = "mbArtistId")
    @Mapping(target = "albums", qualifiedByName = "toAlbumDtoBasic")
    @BeanMapping(ignoreByDefault = true)
    ArtistDto toArtistDto(Artist artist);

    @IterableMapping(qualifiedByName = "toArtistDto")
    List<ArtistDto> toArtistDto(List<Artist> entities);

    @Named("toAlbumDtoBasic")
    @Mapping(target = "id", source = "id")
    @Mapping(target = "album", source = "album")
    @Mapping(target = "year", source = "year")
    @Mapping(target = "genres", qualifiedByName = "toGenreDtoBasic")
    @Mapping(target = "songs", qualifiedByName = "toSongDtoBasic")
    @BeanMapping(ignoreByDefault = true)
    AlbumDto toAlbumDtoBasic(Album entity);

    @Named("toSongDtoBasic")
    @Mapping(target = "id", source = "id")
    @Mapping(target = "title", source = "title")
    @BeanMapping(ignoreByDefault = true)
    SongDto toSongDtoBasic(Song song);


    @Named("toGenreDtoBasic")
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @BeanMapping(ignoreByDefault = true)
    GenreDto toGenreDtoBasic(Genre genre);
}
