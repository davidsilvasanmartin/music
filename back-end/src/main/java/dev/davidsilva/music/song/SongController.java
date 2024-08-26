package dev.davidsilva.music.song;

import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<List<SongDto>> getAllSongs() {
        List<SongDto> songs = songService.findAllSongs();
        return new ResponseEntity<>(songs, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SongDto> getSongById(@PathVariable("id") int id) {
        SongDto song = songService.findSongById(id);
        return new ResponseEntity<>(song, HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping("{id}/albumArt")
    @ResponseBody
    public FileSystemResource getAlbumArtById(@PathVariable("id") int id) {
        return new FileSystemResource(songService.getSongAlbumArtPathById(id));
    }

    @CrossOrigin
    @GetMapping(value = "{id}/play", produces = "audio/*")
    @ResponseBody
    public FileSystemResource playSongById(@PathVariable("id") int id) {
        return new FileSystemResource(songService.getSongFilePathById(id));
    }
}
