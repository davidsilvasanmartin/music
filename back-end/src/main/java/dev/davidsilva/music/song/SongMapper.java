package dev.davidsilva.music.song;

import dev.davidsilva.music.album.Album;
import dev.davidsilva.music.album.AlbumDto;
import dev.davidsilva.music.artist.Artist;
import dev.davidsilva.music.artist.ArtistDto;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface SongMapper {
    @Mapping(target = "album", qualifiedByName = "toAlbumDtoBasic")
    SongDto toSongDto(Song entity);

    @Named("toAlbumDtoBasic")
    @Mapping(target = "id", source = "id")
    @Mapping(target = "album", source = "album")
    @Mapping(target = "artist", qualifiedByName = "toArtistDtoBasic")
    @BeanMapping(ignoreByDefault = true)
    AlbumDto toAlbumDtoBasic(Album entity);

    @Named("toArtistDtoBasic")
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @BeanMapping(ignoreByDefault = true)
    ArtistDto toArtistDto(Artist artist);
}
