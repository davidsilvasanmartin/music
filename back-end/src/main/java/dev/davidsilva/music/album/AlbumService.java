package dev.davidsilva.music.album;

import dev.davidsilva.music.utils.ListMapper;
import dev.davidsilva.music.utils.PaginatedResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class AlbumService {
    private final AlbumRepository albumRepository;
    private final AlbumDtoMapper albumDtoMapper;
    private final ListMapper<Album, AlbumDto> listMapper;

    public AlbumService(AlbumRepository albumRepository, AlbumDtoMapper albumDtoMapper) {
        this.albumRepository = albumRepository;
        this.albumDtoMapper = albumDtoMapper;
        this.listMapper = (albums) -> albums.stream().map(albumDtoMapper::toDto).toList();
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
                new AlbumNotFoundException("Album with id " + id + " was not found")
        );
        return albumDtoMapper.toDto(album);
    }

    public String findAlbumArtPathById(int id) {
        Album album = albumRepository.findById(id).orElseThrow(() ->
                new AlbumNotFoundException("Album with id " + id + " was not found")
        );
        return album.getArtPath();
    }
}
