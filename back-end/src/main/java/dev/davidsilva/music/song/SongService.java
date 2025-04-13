package dev.davidsilva.music.song;

import dev.davidsilva.music.album.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class SongService {
    private final SongRepository songRepository;
    private final SongMapper songMapper;
    private final AlbumRepository albumRepository;
    private final AlbumMapper albumMapper;

    public SongDto findSongById(int id) {
        Song song = songRepository.findById(id).orElseThrow(() ->
                new SongNotFoundException(id));
        return songMapper.toSongDto(song);
    }

    // TODO: test
    public AlbumDto findAlbumBySongId(int songId) {
        Song song = songRepository.findById(songId).orElseThrow(() ->
                new SongNotFoundException(songId));
        int albumId = song.getAlbum().getId();
        Album album = albumRepository.findById(albumId).orElseThrow(() ->
                new AlbumNotFoundException(albumId));
        return albumMapper.toAlbumDto(album);
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
