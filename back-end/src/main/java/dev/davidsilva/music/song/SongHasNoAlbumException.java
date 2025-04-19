package dev.davidsilva.music.song;

/**
 * A song having no album should mean that our database is in an inconsistent state, because
 * one foreign key constraint is violated. However,
 */
public class SongHasNoAlbumException extends RuntimeException {
    public SongHasNoAlbumException(Song song) {
        super("Song " + song.getId() + ", with title \"" + song.getTitle() + "\", has no album");
    }
}
