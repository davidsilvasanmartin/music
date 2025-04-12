package dev.davidsilva.music.album;

import dev.davidsilva.music.song.SongDtoMapper;
import dev.davidsilva.music.utils.DtoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AlbumDtoMapper implements DtoMapper<AlbumDto, Album> {
    private final SongDtoMapper songDtoMapper;

    @Override
    public Album toEntity(AlbumDto dto) {
        throw new UnsupportedOperationException("Not supported yet");
    }

    @Override
    public AlbumDto toDto(Album album) {
        AlbumDto albumDto = new AlbumDto();
        albumDto.setId(album.getId());
        albumDto.setAlbumArtist(album.getArtist() != null ? album.getArtist().getName() : null);
        albumDto.setAlbum(album.getAlbum());
        albumDto.setYear(album.getYear());
        albumDto.setSongs(album.getSongs().stream().map(songDtoMapper::toDtoWithoutAlbum).toList());
        return albumDto;
    }

    public AlbumDto toDtoWithoutSongs(Album album) {
        AlbumDto albumDto = new AlbumDto();
        albumDto.setId(album.getId());
        albumDto.setAlbumArtist(album.getArtist() != null ? album.getArtist().getName() : null);
        albumDto.setAlbum(album.getAlbum());
        albumDto.setYear(album.getYear());
        return albumDto;
    }
}
