package dev.davidsilva.music.album;

import dev.davidsilva.music.artist.Artist;
import dev.davidsilva.music.genre.Genre;
import dev.davidsilva.music.song.Song;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Setter
@Getter
@Entity
@Table(name = "albums")
public class Album implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private int id;

    @Column(name = "beets_id")
    private int beetsId;

    @Column(name = "mb_album_id")
    private String mbAlbumId;

    @Column(name = "art_path")
    private String artPath;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "artist_id", referencedColumnName = "id")
    private Artist artist;


    @Column(name = "album")
    private String album;

    @OneToMany(mappedBy = "album", fetch = FetchType.EAGER)
    @OrderBy("discNumber ASC, trackNumber ASC")
    private List<Song> songs;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "albums_genres",
            joinColumns = @JoinColumn(name = "album_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private Set<Genre> genres;

    @Column(name = "original_year")
    private int year;

    @Column(name = "created_at", insertable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    @PreUpdate
    private void updateTimestamp() {
        this.updatedAt = LocalDateTime.now();
    }
}
