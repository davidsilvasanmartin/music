package dev.davidsilva.music.playlist;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin
@RequestMapping("playlists")
public class PlaylistController {
    private final PlaylistService playlistService;

    /**
     * Gets the list of all playlists. There is no pagination or search, all playlists are returned.
     * <p>
     * TODO !!!!! CONSIDER WHETHER PLAYLISTS SHOULD BELONG TO A USER OR BE GLOBAL !!!!!
     *  IF BELONGING TO A USER, WE NEED TO UPDATE OUR PERMISSION SYSTEM TO INCLUDE
     *  UPDATE_OWN, DELETE_OWN, OR SOMETHING LIKE THAT
     * <p>
     * TODO consider adding pagination/search capabilities to this endpoint
     *
     * @return All playlists belonging to the logged in user
     */
    @GetMapping
    public ResponseEntity<List<PlaylistDto>> getPlaylists(
    ) {
        return new ResponseEntity<>(playlistService.getAllPlaylists(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlaylistDto> getPlaylistById(@PathVariable("id") int id) {
        return new ResponseEntity<>(playlistService.getPlaylistById(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<PlaylistDto> createPlaylist(@RequestBody PlaylistDto playlistDto) {
        return new ResponseEntity<>(playlistService.createPlaylist(playlistDto), HttpStatus.CREATED);
    }

    // TODO find out correct ResponseEntity generic type
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePlaylist(@PathVariable int id) {
        playlistService.deletePlaylist(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
