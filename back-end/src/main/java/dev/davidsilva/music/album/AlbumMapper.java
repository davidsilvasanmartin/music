package dev.davidsilva.music.album;

import dev.davidsilva.music.artist.ArtistMapper;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring", uses = {ArtistMapper.class})
public interface AlbumMapper {
    // TODO artist eventually needs to become an object; need to update Album, AlbumDto
    @Mapping(target = "albumArtist", source = "artist.name")
    // TODO fix the mess of properties
    @Mapping(target = "genres", ignore = true)
    @Mapping(target = "songs", ignore = true)
    AlbumDto toDto(Album entity);

    @Mapping(target = "genres", ignore = true)
    @Mapping(target = "songs", ignore = true)
    List<AlbumDto> toDtos(List<Album> entities);

    // Special mapping method to break circular dependency
    @Named("toDtoWithoutArtist")
    @Mapping(target = "albumArtist", ignore = true)
    @Mapping(target = "genres", ignore = true)
    @Mapping(target = "songs", ignore = true)
    AlbumDto toDtoWithoutArtist(Album entity);

    // Helper method for collections
    @Named("toDtosWithoutArtist")
    @IterableMapping(qualifiedByName = "toDtoWithoutArtist")
    List<AlbumDto> toDtosWithoutArtist(List<Album> entities);

    // Standard entity conversion methods
    // TODO fix this, we don't want to ignore artist
    @Mapping(target = "genres", ignore = true)
    @Mapping(target = "songs", ignore = true)
    Album toEntity(AlbumDto dto);

    List<Album> toEntities(List<AlbumDto> dtos);

    @Named("toEntityWithoutArtist")
    @Mapping(target = "artist", ignore = true)
    @Mapping(target = "genres", ignore = true)
    @Mapping(target = "songs", ignore = true)
    Album toEntityWithoutArtist(AlbumDto dto);

    @Named("toEntitiesWithoutArtist")
    @IterableMapping(qualifiedByName = "toEntityWithoutArtist")
    List<Album> toEntitiesWithoutArtist(List<AlbumDto> dtos);
}
