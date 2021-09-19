package com.example.music.album;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlbumService {
    private final AlbumRepository albumRepository;

    @Autowired
    public AlbumService(AlbumRepository albumRepository) {
        this.albumRepository = albumRepository;
    }

    public List<Album> findAllAlbums() {
        return albumRepository.findAll();
    }

    public Album findAlbumById(int id) {
        return albumRepository.findById(id).orElseThrow(() ->
                new AlbumNotFoundException("Album with id " + id + "was not found")
        );
    }
}
