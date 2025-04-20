package dev.davidsilva.music.playlist;

import dev.davidsilva.music.exception.AbstractNotFoundException;

public class PlaylistItemNotFoundException extends AbstractNotFoundException {
    public PlaylistItemNotFoundException(String id) {
        super("PlaylistItem", id);
    }
}
