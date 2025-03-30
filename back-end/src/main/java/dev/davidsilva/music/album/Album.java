package dev.davidsilva.music.album;

import dev.davidsilva.music.genres.Genre;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
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

    @Column(name = "album_artist")
    private String albumArtist;

    @Column(name = "album")
    private String album;

//    @OneToMany(mappedBy = "album")
//    @JsonManagedReference
//    private List<Song> songs;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "album_genre",
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
