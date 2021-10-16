package blog.cybertricks.music.musicservice.album;

public class AlbumNotFoundException extends RuntimeException {
    public AlbumNotFoundException(String s) {
        super(s);
    }
}
