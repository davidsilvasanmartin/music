package dev.davidsilva.music.album;

import dev.davidsilva.music.song.Song;
import dev.davidsilva.music.song.SongDto;
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
        albumDto.setGenres(Arrays.stream(album.getGenre().split(",")).map(String::trim).toList());
        albumDto.setYear(album.getYear());
        albumDto.setSongs(album.getSongs().stream().map(this::toSongDto).toList());
        return albumDto;
    }

    private SongDto toSongDto(Song song) {
        SongDto songDto = new SongDto();
        songDto.setId(song.getId());
        songDto.setTitle(song.getTitle());
        songDto.setLyrics(song.getLyrics());
        return songDto;
    }
}
