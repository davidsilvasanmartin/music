package dev.davidsilva.music.song;

import dev.davidsilva.music.utils.DtoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
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
//        songDto.setAlbum(albumDtoMapper.toDtoWithoutSongs(song.getAlbum()));
        return songDto;
    }

    public SongDto toDtoWithoutAlbum(Song song) {
        SongDto songDto = new SongDto();
        songDto.setId(song.getId());
        songDto.setTitle(song.getTitle());
        songDto.setLyrics(song.getLyrics());
        return songDto;
    }
}
