package dev.davidsilva.music.album;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class AlbumService {
    private final AlbumRepository albumRepository;
    private final AlbumDtoMapper albumDtoMapper;

    @Autowired
    public AlbumService(AlbumRepository albumRepository, AlbumDtoMapper albumDtoMapper) {
        this.albumRepository = albumRepository;
        this.albumDtoMapper = albumDtoMapper;
    }

    public Page<Album> findAll(AlbumSpecification specification, Pageable pageable) {
        return albumRepository.findAll(specification, pageable);
    }

    public Page<Album> findAll(Pageable pageable) {
        return albumRepository.findAll(pageable);
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
