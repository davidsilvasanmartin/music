package dev.davidsilva.music.song;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SongService {
    private final SongRepository songRepository;

    @Autowired
    public SongService(SongRepository songRepository) {
        this.songRepository = songRepository;
    }

    public List<Song> findAllSongs() {
        return songRepository.findAll();
    }

    public Song findSongById(int id) {
        return songRepository.findById(id).orElseThrow(() ->
                new SongNotFoundException("Album with id " + id + " was not found")
        );
    }
}
