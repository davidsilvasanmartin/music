package dev.davidsilva.music.playlist;

import dev.davidsilva.music.exception.AbstractNotFoundException;

public class PlaylistNotFoundException extends AbstractNotFoundException {
    public PlaylistNotFoundException(int id) {
        super("Playlist", id);
    }
}
