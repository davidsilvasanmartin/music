package dev.davidsilva.music.artist;

import dev.davidsilva.music.album.AlbumMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

// TODO not used (need to change AlbumDto, tests, frontend)
@Mapper(componentModel = "spring", uses = {AlbumMapper.class})
public interface ArtistMapper {
    @Mapping(target = "albums", source = "albums", qualifiedByName = "toAlbumDtosWithoutArtist")
    ArtistDto toArtistDto(Artist entity);

    @Mapping(target = "albums", ignore = true)
    ArtistDto toArtistDtoWithoutAlbums(Artist entity);
}
