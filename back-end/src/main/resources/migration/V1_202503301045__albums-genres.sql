CREATE TABLE IF NOT EXISTS artists
(
    id           SERIAL PRIMARY KEY,
    -- The code implementation assumes the name is unique (because it checks whether an artist with a given name
    -- exists before creating a new one). I'm setting the "name" column as unique because of this. Uniqueness
    -- could be revised in future updates.
    name         VARCHAR(255) NOT NULL UNIQUE,
    mb_artist_id VARCHAR(255) UNIQUE,
    created_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS albums
(
    id            SERIAL PRIMARY KEY,
    beets_id      INTEGER UNIQUE,
    -- TODO consider changing to UUID type, but I would need to check MB's documentation before doing this change
    -- I'm not setting "mb_album_id" as unique because we could have several versions of the same albums, for example
    -- in different formats (I don't know how the application code would handle this case though)
    -- TODO check and test the case of several albums with the same mb_album_id
    mb_album_id   VARCHAR(255),
    art_path      VARCHAR(255),
    -- TODO I believe Musicbrainz creates new artist UUIDs for the case where multiple artists collaborate to
    --  record one album. I believe beets will always only provide one artist id too, but I need to test this.
    --  For now I'm leaving this as a one-to-many relationship, but it could be changed to many-to-many later
    artist_id     INTEGER,
    album         VARCHAR(255),
    original_year INTEGER,
    created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_albums_artist
        FOREIGN KEY (artist_id) REFERENCES artists (id)
);

CREATE TABLE IF NOT EXISTS genres
(
    id         SERIAL PRIMARY KEY,
    name       VARCHAR(255) NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS albums_genres
(
    album_id INTEGER NOT NULL,
    genre_id INTEGER NOT NULL,
    PRIMARY KEY (album_id, genre_id),
    FOREIGN KEY (album_id) REFERENCES albums (id) ON DELETE CASCADE,
    -- When trying to delete a genre that's still referenced by albums, the operation will be restricted
    FOREIGN KEY (genre_id) REFERENCES genres (id) ON DELETE RESTRICT
);

CREATE INDEX idx_artists_name ON artists (name);
CREATE INDEX idx_artists_mb_artist_id ON artists (mb_artist_id);
CREATE INDEX idx_albums_album ON albums (album);
CREATE INDEX idx_albums_album_artist ON albums (artist_id);
CREATE INDEX idx_albums_beets_id ON albums (beets_id);
CREATE INDEX idx_albums_mb_album_id ON albums (mb_album_id);
CREATE INDEX idx_genres_name ON genres (name);