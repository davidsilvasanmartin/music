package dev.davidsilva.music.song;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("songs")
public class SongController {
    private final SongService songService;

    public SongController(SongService songService) {
        this.songService = songService;
    }

    @GetMapping
    public ResponseEntity<List<Song>> getAllSongs() {
        List<Song> songs = songService.findAllSongs();
        return new ResponseEntity<>(songs, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Song> getSongById(@PathVariable("id") int id) {
        Song song = songService.findSongById(id);
        return new ResponseEntity<>(song, HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping("{id}/albumArt")
    @ResponseBody
    public FileSystemResource getAlbumArtById(@PathVariable("id") int id) {
        Song song = songService.findSongById(id);
        return new FileSystemResource(song.getAlbum().getArtPath());
    }

    @CrossOrigin
    @GetMapping(value = "{id}/play", produces = "audio/*")
    @ResponseBody
    public FileSystemResource playSongById(@PathVariable("id") int id) {
        Song song = songService.findSongById(id);
        return new FileSystemResource(song.getPath());
    }
}
