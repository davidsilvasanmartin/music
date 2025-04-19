package dev.davidsilva.music.playlist;

import dev.davidsilva.music.exception.AbstractNotFoundException;

public class PlaylistItemNotFoundException extends AbstractNotFoundException {
    public PlaylistItemNotFoundException(int id) {
        super("PlaylistItem", id);
    }
}
