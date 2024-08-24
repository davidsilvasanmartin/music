package dev.davidsilva.music.album;

import dev.davidsilva.music.utils.SearchCriteria;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@CrossOrigin
@RequestMapping("albums")
public class AlbumController {
    private final AlbumService albumService;

    public AlbumController(AlbumService albumService) {
        this.albumService = albumService;
    }

    @GetMapping
    public ResponseEntity<Page<Album>> getAlbums(
            @PageableDefault(page = 0, size = 10)
            @SortDefault.SortDefaults({
                    @SortDefault(sort = "albumArtist", direction = Sort.Direction.ASC),
                    @SortDefault(sort = "year", direction = Sort.Direction.ASC)
            })
                    Pageable pageable,
            @RequestParam(value = "search", required = false) String search
    ) {
        Page<Album> paginatedAlbums;
        // TODO: not working
        if (search != null) {
            Pattern pattern = Pattern.compile("(\\w+)(:)(\\w+)");
            Matcher matcher = pattern.matcher(search);
            matcher.find();
            SearchCriteria searchCriteria = new SearchCriteria(matcher.group(1), matcher.group(2),
                    matcher.group(3));
            AlbumSpecification albumSpecification = new AlbumSpecification(searchCriteria);
            paginatedAlbums = albumService.findAll(albumSpecification, pageable);
        } else {
            paginatedAlbums = albumService.findAll(pageable);
        }
        return new ResponseEntity<>(paginatedAlbums, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Album> getAlbumById(@PathVariable("id") int id) {
        Album album = albumService.findAlbumById(id);
        return new ResponseEntity<>(album, HttpStatus.OK);
    }

    @GetMapping("{id}/albumArt")
    @ResponseBody
    public FileSystemResource getAlbumArtById(@PathVariable("id") int id) {
        Album album = albumService.findAlbumById(id);
        // TODO: album.getArtPath() can be null
        return new FileSystemResource(album.getArtPath());
    }
}
