package dev.davidsilva.music.album;

import dev.davidsilva.music.utils.DtoMapper;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class AlbumDtoMapper implements DtoMapper<AlbumDto, Album> {
    @Override
    public Album toEntity(AlbumDto dto) {
        return null;
    }

    @Override
    public AlbumDto toDto(Album album) {
        AlbumDto albumDto = new AlbumDto();
        albumDto.setId(album.getId());
        albumDto.setAlbumArtist(album.getAlbumArtist());
        albumDto.setAlbum(album.getAlbum());
        albumDto.setGenres(Arrays.stream(album.getGenre().split(",")).toList());
        albumDto.setYear(album.getYear());
        return albumDto;
    }
}
