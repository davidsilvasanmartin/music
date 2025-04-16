package dev.davidsilva.music.artist;

import dev.davidsilva.music.exception.AbstractNotFoundException;

public class ArtistNotFoundException extends AbstractNotFoundException {
    public ArtistNotFoundException(int id) {
        super("Artist with id " + id + " was not found");
    }
}
