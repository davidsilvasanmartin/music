CREATE TABLE playlists
(
    id          SERIAL PRIMARY KEY,
    name        VARCHAR(255) NOT NULL,
    description TEXT,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_playlists_name ON playlists (name);

-- Fully denormalized playlist items table
CREATE TABLE playlist_items
(
    id          SERIAL PRIMARY KEY,
    playlist_id INTEGER      NOT NULL,
    position    INTEGER      NOT NULL, -- To maintain order within the playlist

    -- Link to the local song table (nullable!).
    -- ON DELETE SET NULL means if the song is deleted, this field becomes NULL,
    -- but the playlist_item row itself remains.
    song_id     INTEGER      NULL,

    -- Cached/Denormalized data (essential for persistence). We want to keep these values
    -- so the playlist can be used when the song is removed from the Beets database,
    -- and this app's database is updated to remove the song. This data is, of course,
    -- going to be duplicated if the same song belongs to many playlists.
    -- TODO if we allow to edit albums or songs, we need to change the titles here, if they
    --  get updated
    -- The cached data could be in a separate table but this would complicate logic
    mb_track_id VARCHAR(255) NOT NULL, -- The persistent MusicBrainz ID
    song_title  VARCHAR(255) NOT NULL, -- Cached title
    mb_album_id VARCHAR(255),          -- Cached album MusicBrainz ID
    album_title VARCHAR(255),          -- Cached album title

    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_playlist_items_playlist
        FOREIGN KEY (playlist_id) REFERENCES playlists (id)
            ON DELETE CASCADE,         -- If a playlist is deleted, its items are deleted too

    CONSTRAINT fk_playlist_items_song
        FOREIGN KEY (song_id) REFERENCES songs (id)
            ON DELETE SET NULL,        -- Crucial part for persistence!

    -- Ensure position is unique within a playlist
    UNIQUE (playlist_id, position)
);

CREATE INDEX idx_playlist_items_playlist_id ON playlist_items (playlist_id);
CREATE INDEX idx_playlist_items_song_id ON playlist_items (song_id);
CREATE INDEX idx_playlist_items_mb_track_id ON playlist_items (mb_track_id);