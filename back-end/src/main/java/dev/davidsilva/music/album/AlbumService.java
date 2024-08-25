package dev.davidsilva.music.album;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class AlbumService {
    private final AlbumRepository albumRepository;

    @Autowired
    public AlbumService(AlbumRepository albumRepository) {
        this.albumRepository = albumRepository;
    }

    public Page<Album> findAll(AlbumSpecification specification, Pageable pageable) {
        return albumRepository.findAll(specification, pageable);
    }

    public Page<Album> findAll(Pageable pageable) {
        return albumRepository.findAll(pageable);
    }

    public Album findAlbumById(int id) {
        return albumRepository.findById(id).orElseThrow(() ->
                new AlbumNotFoundException("Album with id " + id + " was not found")
        );
    }
}
