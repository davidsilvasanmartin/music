package dev.davidsilva.music.song;

import dev.davidsilva.music.exception.AbstractNotFoundException;

public class SongNotFoundException extends AbstractNotFoundException {
    public SongNotFoundException(int id) {
        super("Song", id);
    }
}
