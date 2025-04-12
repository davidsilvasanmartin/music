package dev.davidsilva.music.album;

import dev.davidsilva.music.search.SearchStringMapper;
import dev.davidsilva.music.utils.PaginatedResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

@RestController
@RequiredArgsConstructor
@CrossOrigin
@RequestMapping("albums")
public class AlbumController {
    private final AlbumService albumService;
    private final SearchStringMapper searchStringMapper;

    @GetMapping
    public ResponseEntity<PaginatedResponse<AlbumDto>> getAlbums(
            // This default has to be 0 even if we are using one-indexed parameters. Spring uses number 0
            // for the first page in the Pageable object always, the one-indexed parameter option only
            // means that a 1 from the front-end is transformed into a 0 on the back-end
            @PageableDefault(page = 0, size = 10)
            @SortDefault.SortDefaults({
                    @SortDefault(sort = "artist.name", direction = Sort.Direction.ASC),
                    @SortDefault(sort = "year", direction = Sort.Direction.ASC)
            })
            Pageable pageable,
            @RequestParam(value = "search", required = false) String search
    ) {
        PaginatedResponse<AlbumDto> paginatedAlbums;
        if (search != null) {
            AlbumSpecification albumSpecification = new AlbumSpecification(searchStringMapper.toSearchCriteria(search));
            paginatedAlbums = albumService.findAll(albumSpecification, pageable);
        } else {
            paginatedAlbums = albumService.findAll(pageable);
        }
        return new ResponseEntity<>(paginatedAlbums, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AlbumDto> getAlbumById(@PathVariable("id") int id) {
        AlbumDto album = albumService.findAlbumById(id);
        return new ResponseEntity<>(album, HttpStatus.OK);
    }

    @GetMapping(value = "{id}/albumArt", produces = {MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_GIF_VALUE})
    @ResponseBody
    public ResponseEntity<FileSystemResource> getAlbumArtById(@PathVariable("id") int id) {
        HttpHeaders headers = new HttpHeaders();
        headers.setCacheControl(CacheControl.maxAge(Duration.ofDays(60)).cachePublic());
        FileSystemResource resource = new FileSystemResource(albumService.findAlbumArtPathById(id));
        // TODO: album.getArtPath() can be null
        return new ResponseEntity<>(resource, headers, HttpStatus.OK);
    }
}
