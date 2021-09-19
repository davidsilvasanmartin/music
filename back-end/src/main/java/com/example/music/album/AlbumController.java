package com.example.music.album;

import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("api/v1/albums")
public class AlbumController {
    private final AlbumService albumService;

    public AlbumController(AlbumService albumService) {
        this.albumService = albumService;
    }

    @GetMapping
    public ResponseEntity<List<Album>> getAllAlbums() {
        List<Album> albums = albumService.findFirst2Albums();
        return new ResponseEntity<>(albums, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Album> getAlbumById(@PathVariable("id") int id) {
        Album album = albumService.findAlbumById(id);
        return new ResponseEntity<>(album, HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping("{id}/albumArt")
    @ResponseBody
    public FileSystemResource getAlbumArtById(@PathVariable("id") int id) {
        Album album = albumService.findAlbumById(id);
        return new FileSystemResource(album.getArtPath());
    }
}
