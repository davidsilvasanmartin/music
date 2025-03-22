CREATE TABLE IF NOT EXISTS playlists
(
    playlist_id INTEGER PRIMARY KEY AUTOINCREMENT,
    name        VARCHAR(100) NOT NULL,
    user_id     INTEGER      NOT NULL,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES auth_users (user_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS playlist_songs
(
    id          INTEGER PRIMARY KEY AUTOINCREMENT,
    playlist_id INTEGER NOT NULL,
    song_id     INTEGER NOT NULL,
    position    INTEGER NOT NULL,
    added_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (playlist_id, song_id),
    FOREIGN KEY (playlist_id) REFERENCES playlists (playlist_id) ON DELETE CASCADE
);

-- Create indices for improved query performance
CREATE INDEX idx_playlist_songs_playlist_id ON playlist_songs (playlist_id);
CREATE INDEX idx_playlist_songs_song_id ON playlist_songs (song_id);
CREATE INDEX idx_playlists_user_id ON playlists (user_id);
