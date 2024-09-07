package dev.davidsilva.music.song;

import dev.davidsilva.music.album.AlbumDto;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
@CrossOrigin
@RequestMapping("songs")
public class SongController {
    private final SongService songService;

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

    @GetMapping("/{id}/album")
    public ResponseEntity<AlbumDto> getAlbumBySongId(@PathVariable("id") int id) {
        AlbumDto album = songService.findSongAlbumById(id);
        return new ResponseEntity<>(album, HttpStatus.OK);
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
    public ResponseEntity<FileSystemResource> playSongById(@PathVariable("id") int id) throws IOException {
        FileSystemResource resource = new FileSystemResource(songService.getSongFilePathById(id));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentLength(resource.contentLength());
        // TODO: instead of using the file's extension, we can use the 'format' column of the database
        String resourceExtension = Objects.requireNonNull(resource.getFilename()).substring(resource.getFilename().lastIndexOf(".") + 1);
        // For a list of media types, see https://developer.mozilla.org/en-US/docs/Web/HTTP/Basics_of_HTTP/MIME_types/Common_types
        switch (resourceExtension.toLowerCase()) {
            case "mp3":
                headers.setContentType(MediaType.parseMediaType("audio/mpeg"));
                break;
            case "flac":
                // See https://stackoverflow.com/questions/67078696/which-is-the-correct-content-type-for-flac
                headers.setContentType(MediaType.parseMediaType("audio/flac"));
                break;
            case "m4a":
                // https://stackoverflow.com/questions/39885749/is-a-m4a-file-considered-as-of-mime-type-audio-m4a-or-audio-mp4
                headers.setContentType(MediaType.parseMediaType("audio/mp4"));
                break;
            case "ogg":
                headers.setContentType(MediaType.parseMediaType("audio/ogg"));
                break;
            default:
                throw new SongFormatNotSupportedException(resourceExtension);
        }
        return new ResponseEntity<>(resource, headers, HttpStatus.OK);
    }
}
