package dev.davidsilva.music.song;

import dev.davidsilva.music.album.AlbumDtoMapper;
import dev.davidsilva.music.album.AlbumRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class SongService {
    //    private final SongRepository songRepository;
    private final SongDtoMapper songDtoMapper;
    private final AlbumRepository albumRepository;
    private final AlbumDtoMapper albumDtoMapper;

//    public SongDto findSongById(int id) {
//        Song song = songRepository.findById(id).orElseThrow(() ->
//                new SongNotFoundException(id));
//        return songDtoMapper.toDto(song);
//    }

    // TODO: test
//    public AlbumDto findAlbumBySongId(int songId) {
//        Song song = songRepository.findById(songId).orElseThrow(() ->
//                new SongNotFoundException(songId));
//        int albumId = song.getAlbum().getId();
//        Album album = albumRepository.findById(albumId).orElseThrow(() ->
//                new AlbumNotFoundException(albumId));
//        return albumDtoMapper.toDto(album);
//    }

//    public String getSongAlbumArtPathById(int id) {
//        Song song = songRepository.findById(id).orElseThrow(() ->
//                new SongNotFoundException(id));
//        return song.getAlbum().getArtPath();
//    }

//    public String getSongFilePathById(int id) {
//        Song song = songRepository.findById(id).orElseThrow(() ->
//                new SongNotFoundException(id));
//        return song.getPath();
//    }
}
