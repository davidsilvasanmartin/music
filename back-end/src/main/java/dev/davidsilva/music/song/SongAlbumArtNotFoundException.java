package dev.davidsilva.music.song;

import dev.davidsilva.music.exception.AbstractNotFoundException;

public class SongAlbumArtNotFoundException extends AbstractNotFoundException {
    public SongAlbumArtNotFoundException(int songId) {
        super("Album art for song", songId);
    }
}
