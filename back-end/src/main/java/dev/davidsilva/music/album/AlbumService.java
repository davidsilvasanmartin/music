package dev.davidsilva.music.album;

import dev.davidsilva.music.utils.ListMapper;
import dev.davidsilva.music.utils.PaginatedResponse;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class AlbumService {
    private final AlbumRepository albumRepository;
    private final AlbumMapper albumMapper;
    private final ListMapper<Album, AlbumDto> listMapper;

    public AlbumService(AlbumRepository albumRepository, AlbumMapper albumMapper) {
        this.albumRepository = albumRepository;
        this.albumMapper = albumMapper;
        this.listMapper = (albums) -> albums.stream().map(albumMapper::toDto).toList();
    }

    public PaginatedResponse<AlbumDto> findAll(AlbumSpecification specification, Pageable pageable) {
        Page<Album> albumsPage = albumRepository.findAll(specification, pageable);
        return PaginatedResponse.fromPage(albumsPage, listMapper);
    }

    public PaginatedResponse<AlbumDto> findAll(Pageable pageable) {
        Page<Album> albumsPage = albumRepository.findAll(pageable);
        return PaginatedResponse.fromPage(albumsPage, listMapper);
    }

    public AlbumDto findAlbumById(int id) {
        Album album = albumRepository.findById(id).orElseThrow(() ->
                new AlbumNotFoundException(id)
        );
        return albumMapper.toDto(album);
    }

    public String findAlbumArtPathById(int id) {
        Album album = albumRepository.findById(id).orElseThrow(() ->
                new AlbumNotFoundException(id)
        );
        return album.getArtPath();
    }
}
