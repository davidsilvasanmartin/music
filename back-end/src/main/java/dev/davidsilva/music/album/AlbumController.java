package dev.davidsilva.music.album;

import dev.davidsilva.music.search.SearchStringMapper;
import dev.davidsilva.music.utils.PaginatedResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@CrossOrigin
@RequestMapping("albums")
public class AlbumController {
    private final AlbumService albumService;
    private final SearchStringMapper searchStringMapper;

    @GetMapping
    public ResponseEntity<PaginatedResponse<AlbumDto>> getAlbums(
            @PageableDefault(page = 0, size = 10)
            @SortDefault.SortDefaults({
                    @SortDefault(sort = "albumArtist", direction = Sort.Direction.ASC),
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

    @GetMapping("{id}/albumArt")
    @ResponseBody
    public FileSystemResource getAlbumArtById(@PathVariable("id") int id) {
        String artPath = albumService.findAlbumArtPathById(id);
        // TODO: album.getArtPath() can be null
        return new FileSystemResource(artPath);
    }
}
