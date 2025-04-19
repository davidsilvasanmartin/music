package dev.davidsilva.music.album;

import dev.davidsilva.music.exception.AbstractNotFoundException;

public class AlbumNotFoundException extends AbstractNotFoundException {
    public AlbumNotFoundException(int id) {
        super("Album", id);
    }
}
