package dev.davidsilva.music.artist;

import dev.davidsilva.music.album.AlbumMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {AlbumMapper.class})
public interface ArtistMapper {
    Artist toEntity(ArtistDto dto);

    @Mapping(target = "albums", source = "albums", qualifiedByName = "toDtosWithoutArtist")
    ArtistDto toDtoWithAlbums(Artist entity);

    @Mapping(target = "albums", ignore = true)
    ArtistDto toDtoWithoutAlbums(Artist entity);
}
