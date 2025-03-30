package dev.davidsilva.music.song;

import dev.davidsilva.music.album.Album;
import dev.davidsilva.music.album.AlbumDto;
import dev.davidsilva.music.utils.DtoMapper;
import org.springframework.stereotype.Component;

@Component
public class SongDtoMapper implements DtoMapper<SongDto, Song> {
    @Override
    public Song toEntity(SongDto dto) {
        throw new UnsupportedOperationException("Not supported yet");
    }

    @Override
    public SongDto toDto(Song song) {
        SongDto songDto = new SongDto();
        songDto.setId(song.getId());
        songDto.setTitle(song.getTitle());
        songDto.setLyrics(song.getLyrics());
        // songDto.setAlbum(albumToDto(song.getAlbum()));
        return songDto;
    }

    private AlbumDto albumToDto(Album album) {
        AlbumDto albumDto = new AlbumDto();
        albumDto.setId(album.getId());
        albumDto.setAlbumArtist(album.getAlbumArtist());
        albumDto.setAlbum(album.getAlbum());
        return albumDto;
    }
}
