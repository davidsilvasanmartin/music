package dev.davidsilva.music.song;

import dev.davidsilva.music.album.AlbumDto;
import dev.davidsilva.music.album.AlbumDtoMapper;
import dev.davidsilva.music.utils.ListMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SongService {
    private final SongRepository songRepository;
    private final SongDtoMapper songDtoMapper;
    private final AlbumDtoMapper albumDtoMapper;
    private final ListMapper<Song, SongDto> listMapper;

    public SongService(SongRepository songRepository, SongDtoMapper songDtoMapper, AlbumDtoMapper albumDtoMapper) {
        this.songRepository = songRepository;
        this.songDtoMapper = songDtoMapper;
        this.albumDtoMapper = albumDtoMapper;
        this.listMapper = (songs) -> songs.stream().map(songDtoMapper::toDto).toList();
    }

    // TODO pagination, searching, sorting
    public List<SongDto> findAllSongs() {
        List<Song> songs = songRepository.findAll();
        return listMapper.map(songs);
    }

    public SongDto findSongById(int id) {
        Song song = songRepository.findById(id).orElseThrow(() ->
                new SongNotFoundException("Song with id " + id + " was not found"));
        return songDtoMapper.toDto(song);
    }

    public AlbumDto findSongAlbumById(int id) {
        Song song = songRepository.findById(id).orElseThrow(() ->
                new SongNotFoundException("Song with id " + id + " was not found"));
        return albumDtoMapper.toDto(song.getAlbum());
    }

    public String getSongAlbumArtPathById(int id) {
        Song song = songRepository.findById(id).orElseThrow(() ->
                new SongNotFoundException("Song with id " + id + " was not found"));
        return song.getAlbum().getArtPath();
    }

    public String getSongFilePathById(int id) {
        Song song = songRepository.findById(id).orElseThrow(() ->
                new SongNotFoundException("Song with id " + id + " was not found"));
        return song.getPath();
    }
}
