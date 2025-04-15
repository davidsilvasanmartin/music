package dev.davidsilva.music.song;

import dev.davidsilva.music.exception.AbstractNotFoundException;

public class SongAlbumArtNotFoundException extends AbstractNotFoundException {
    public SongAlbumArtNotFoundException(int albumId) {
        super("Album art not found for song with id " + albumId);
    }
}
