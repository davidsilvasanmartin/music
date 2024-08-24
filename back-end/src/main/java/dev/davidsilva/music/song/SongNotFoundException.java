package dev.davidsilva.music.song;

public class SongNotFoundException extends RuntimeException {
    public SongNotFoundException(String s) {
        super(s);
    }
}
