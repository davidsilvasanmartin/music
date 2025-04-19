package dev.davidsilva.music.playlist;

import dev.davidsilva.music.song.Song;
import dev.davidsilva.music.song.SongDto;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PlaylistMapper {
    @Named("toPlaylistDto")
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "items", qualifiedByName = "toPlaylistItemDtoBasic")
    @BeanMapping(ignoreByDefault = true)
    PlaylistDto toPlaylistDto(Playlist entity);

    @IterableMapping(qualifiedByName = "toPlaylistDto")
    List<PlaylistDto> toPlaylistDto(List<Playlist> entities);

    @Named("toPlaylistItemDtoBasic")
    @Mapping(target = "id", source = "id")
    @Mapping(target = "position", source = "position")
    @Mapping(target = "mbTrackId", source = "mbTrackId")
    @Mapping(target = "songTitle", source = "songTitle")
    @Mapping(target = "mbAlbumId", source = "mbAlbumId")
    @Mapping(target = "albumTitle", source = "albumTitle")
    @Mapping(target = "song", qualifiedByName = "toSongDtoBasic")
    @BeanMapping(ignoreByDefault = true)
    SongDto toPlaylistItemDtoBasic(Song song);

    // Ignoring other interesting Song fields, because they are supposed to have
    // been cached in the PlaylistItem entity
    @Named("toSongDtoBasic")
    @Mapping(target = "id", source = "id")
    @BeanMapping(ignoreByDefault = true)
    SongDto toSongDtoBasic(Song song);

    @Named("toPlaylist")
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "items", qualifiedByName = "toPlaylistItemBasic")
    @BeanMapping(ignoreByDefault = true)
    Playlist toPlaylist(PlaylistDto dto);

    @Named("toPlaylistItemBasic")
    @Mapping(target = "id", source = "id")
    @Mapping(target = "position", source = "position")
    @Mapping(target = "mbTrackId", source = "mbTrackId")
    @Mapping(target = "songTitle", source = "songTitle")
    @Mapping(target = "mbAlbumId", source = "mbAlbumId")
    @Mapping(target = "albumTitle", source = "albumTitle")
    @Mapping(target = "song", qualifiedByName = "toSongBasic")
    @BeanMapping(ignoreByDefault = true)
    PlaylistItem toPlaylistItemBasic(PlaylistItemDto dto);

    @Named("toSongBasic")
    @Mapping(target = "id", source = "id")
    @BeanMapping(ignoreByDefault = true)
    Song toSongBasic(SongDto dto);
}
