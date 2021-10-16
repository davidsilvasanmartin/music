package blog.cybertricks.music.musicservice.song;

public class SongNotFoundException extends RuntimeException {
    public SongNotFoundException(String s) {
        super(s);
    }
}
