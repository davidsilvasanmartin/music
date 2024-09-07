package dev.davidsilva.music.song;

import dev.davidsilva.music.album.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SongService {
    private final SongRepository songRepository;
    private final SongDtoMapper songDtoMapper;
    private final AlbumRepository albumRepository;
    private final AlbumDtoMapper albumDtoMapper;

    public SongDto findSongById(int id) {
        Song song = songRepository.findById(id).orElseThrow(() ->
                new SongNotFoundException(id));
        return songDtoMapper.toDto(song);
    }

    public AlbumDto findSongAlbumById(int id) {
        Album album = albumRepository.findById(id).orElseThrow(() ->
                new AlbumNotFoundException(id));
        return albumDtoMapper.toDto(album);
    }

    public String getSongAlbumArtPathById(int id) {
        Song song = songRepository.findById(id).orElseThrow(() ->
                new SongNotFoundException(id));
        return song.getAlbum().getArtPath();
    }

    public String getSongFilePathById(int id) {
        Song song = songRepository.findById(id).orElseThrow(() ->
                new SongNotFoundException(id));
        return song.getPath();
    }
}
