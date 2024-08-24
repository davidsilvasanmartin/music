package dev.davidsilva.music.album;

public class AlbumNotFoundException extends RuntimeException {
    public AlbumNotFoundException(String s) {
        super(s);
    }
}
