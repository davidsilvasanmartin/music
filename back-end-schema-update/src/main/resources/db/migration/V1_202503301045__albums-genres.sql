CREATE TABLE IF NOT EXISTS albums
(
    id            SERIAL PRIMARY KEY,
    beets_id      INTEGER UNIQUE,
    -- TODO consider changing to UUID type, but I would need to check MB's documentation before doing this change
    mb_album_id   VARCHAR(255),
    art_path      VARCHAR(255),
    album_artist  VARCHAR(255),
    album         VARCHAR(255),
    original_year INTEGER,
    created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS genres
(
    id         SERIAL PRIMARY KEY,
    name       VARCHAR(255) NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS album_genre
(
    album_id INTEGER NOT NULL,
    genre_id INTEGER NOT NULL,
    PRIMARY KEY (album_id, genre_id),
    FOREIGN KEY (album_id) REFERENCES albums (id) ON DELETE CASCADE,
    -- When trying to delete a genre that's still referenced by albums, the operation will be restricted
    FOREIGN KEY (genre_id) REFERENCES genres (id) ON DELETE RESTRICT
);

CREATE INDEX idx_albums_album ON albums (album);
CREATE INDEX idx_albums_album_artist ON albums (album_artist);
CREATE INDEX idx_albums_beets_id ON albums (beets_id);
CREATE INDEX idx_albums_mb_album_id ON albums (mb_album_id);
CREATE INDEX idx_genres_name ON genres (name);