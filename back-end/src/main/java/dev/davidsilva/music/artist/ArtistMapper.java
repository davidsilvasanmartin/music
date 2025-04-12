package dev.davidsilva.music.artist;

import dev.davidsilva.music.album.AlbumDto;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ArtistMapper {
    Artist toEntity(ArtistDto dto);

    @Mapping(target = "albums", qualifiedByName = "mapAlbums")
    ArtistDto toDtoWithAlbums(Artist entity);

    @AfterMapping
    @Named("mapAlbums")
    default void mapAlbums(Artist entity, @MappingTarget ArtistDto dto) {
        if (entity.getAlbums() == null) {
            return;
        }

        List<AlbumDto> albumDtos = entity.getAlbums().stream()
                .map(album -> {
                    AlbumDto albumDto = new AlbumDto();
                    albumDto.setId(album.getId());
                    albumDto.setAlbum(album.getAlbum());
                    albumDto.setYear(album.getYear());
                    // Set album artist name instead of the full artist object
                    albumDto.setAlbumArtist(entity.getName());

                    // TODO: Map other album properties as needed
                    // Note: songs and genres mapping would go here if needed

                    return albumDto;
                }).toList();
        dto.setAlbums(albumDtos);
    }

    @Mapping(target = "albums", ignore = true)
    ArtistDto toDtoWithoutAlbums(Artist entity);
}
