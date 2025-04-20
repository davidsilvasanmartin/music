package dev.davidsilva.music.playlist;

import dev.davidsilva.music.exception.AbstractValidationException;

public class PlaylistItemNullSongOnCreationException extends AbstractValidationException {
    public PlaylistItemNullSongOnCreationException() {
        super("Playlist items must be associated with a song on creation");
    }
}
