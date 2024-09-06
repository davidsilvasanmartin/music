package dev.davidsilva.music.album;

public class AlbumNotFoundException extends RuntimeException {
    public AlbumNotFoundException(int id) {
        super("Album with id " + id + " was not found");
    }
}
