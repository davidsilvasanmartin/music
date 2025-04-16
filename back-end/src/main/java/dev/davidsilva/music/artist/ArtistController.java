package dev.davidsilva.music.artist;

import dev.davidsilva.music.search.SearchStringMapper;
import dev.davidsilva.music.utils.PaginatedResponse;
import lombok.RequiredArgsConstructor;
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
@RequestMapping("artists")
public class ArtistController {
    private final ArtistService artistService;
    private final SearchStringMapper searchStringMapper;

    @GetMapping
    public ResponseEntity<PaginatedResponse<ArtistDto>> getArtists(
            @PageableDefault(page = 0, size = 10)
            @SortDefault.SortDefaults({
                    @SortDefault(sort = "name", direction = Sort.Direction.ASC),
            })
            Pageable pageable,
            @RequestParam(value = "search", required = false) String search
    ) {
        PaginatedResponse<ArtistDto> paginatedArtists;
        if (search != null) {
            ArtistSpecification artistSpecification = new ArtistSpecification(searchStringMapper.toSearchCriteria(search));
            paginatedArtists = artistService.findAll(artistSpecification, pageable);
        } else {
            paginatedArtists = artistService.findAll(pageable);
        }
        return new ResponseEntity<>(paginatedArtists, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArtistDto> getArtistById(@PathVariable("id") int id) {
        ArtistDto artist = artistService.findArtistById(id);
        return new ResponseEntity<>(artist, HttpStatus.OK);
    }
}
