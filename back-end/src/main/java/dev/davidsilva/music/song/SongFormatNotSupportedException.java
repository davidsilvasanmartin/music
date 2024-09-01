package dev.davidsilva.music.song;

public class SongFormatNotSupportedException extends RuntimeException {
    public SongFormatNotSupportedException(String songFormat) {
        super("Song format not supported: " + songFormat);
    }
}
