package dev.davidsilva.music.album;

import dev.davidsilva.music.exception.AbstractNotFoundException;

public class AlbumArtNotFoundException extends AbstractNotFoundException {
    public AlbumArtNotFoundException(int albumId) {
        super("Album art not found for album with id " + albumId);
    }
}
