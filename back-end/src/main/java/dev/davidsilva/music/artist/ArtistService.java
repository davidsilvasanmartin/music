package dev.davidsilva.music.artist;

import dev.davidsilva.music.artist.ArtistSpecification;
import dev.davidsilva.music.utils.ListMapper;
import dev.davidsilva.music.utils.PaginatedResponse;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class ArtistService {
    private final ArtistRepository artistRepository;
    private final ArtistMapper artistMapper;
    private final ListMapper<Artist, ArtistDto> listMapper;

    public ArtistService(ArtistRepository artistRepository, ArtistMapper artistMapper) {
        this.artistRepository = artistRepository;
        this.artistMapper = artistMapper;
        this.listMapper = artistMapper::toArtistDto;
    }

    public PaginatedResponse<ArtistDto> findAll(ArtistSpecification specification, Pageable pageable) {
        Page<Artist> artistsPage = artistRepository.findAll(specification, pageable);
        return PaginatedResponse.fromPage(artistsPage, listMapper);
    }

    public PaginatedResponse<ArtistDto> findAll(Pageable pageable) {
        Page<Artist> artistsPage = artistRepository.findAll(pageable);
        return PaginatedResponse.fromPage(artistsPage, listMapper);
    }

    public ArtistDto findArtistById(int id) {
        Artist artist = artistRepository.findById(id).orElseThrow(() ->
                new ArtistNotFoundException(id)
        );
        return artistMapper.toArtistDto(artist);
    }
}
