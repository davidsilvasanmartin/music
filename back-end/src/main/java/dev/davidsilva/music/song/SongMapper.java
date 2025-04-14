package dev.davidsilva.music.song;

import dev.davidsilva.music.album.Album;
import dev.davidsilva.music.album.AlbumDto;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface SongMapper {
    @Mapping(target = "album", qualifiedByName = "toAlbumDtoBasic")
    SongDto toSongDto(Song entity);

    // TODO include artist here ??
    @Named("toAlbumDtoBasic")
    @Mapping(target = "id", source = "id")
//    @Mapping(target = "albumArtist", source = "artist.name")
    @Mapping(target = "album", source = "album")
    @BeanMapping(ignoreByDefault = true)
    AlbumDto toAlbumDtoBasic(Album entity);
}
