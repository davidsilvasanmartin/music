CREATE TABLE songs
(
    id                 SERIAL PRIMARY KEY,
    beets_id           INT UNIQUE NOT NULL,
    mb_track_id        VARCHAR(255),
    path               VARCHAR(1024),
    album_id           INT,
    title              VARCHAR(255),
    track_number       INT,
    track_number_total INT,
    disc_number        INT,
    disc_number_total  INT,
    lyrics             TEXT,
    created_at         TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at         TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_album FOREIGN KEY (album_id)
        REFERENCES albums (id)
        ON DELETE CASCADE
);

CREATE INDEX idx_songs_beets_id ON songs (beets_id);
CREATE INDEX idx_songs_album_id ON songs (album_id);
CREATE INDEX idx_songs_title ON songs (title);

COMMENT ON TABLE songs IS 'Stores song information imported from the Beets library';