package com.example.music.song;

import org.springframework.core.io.FileSystemResource;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/songs/play")
public class PlaySongController {
    private final SongService songService;

    public PlaySongController(SongService songService) {
        this.songService = songService;
    }

    @CrossOrigin
    @GetMapping("/{id}")
    @ResponseBody
    public FileSystemResource getAlbumById(@PathVariable("id") int id) {
        Song song = songService.findSongById(id);
        return new FileSystemResource(song.getPath());
    }
}
