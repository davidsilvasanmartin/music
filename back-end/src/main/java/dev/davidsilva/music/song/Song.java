package dev.davidsilva.music.song;

import dev.davidsilva.music.album.Album;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name = "songs")
public class Song implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private int id;

    @Column(name = "beets_id")
    private int beetsId;

    @Column(name = "mb_track_id")
    private String mbTrackId;

    @Column(name = "path")
    private String path;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "album_id",
            referencedColumnName = "id"
    )
    private Album album;

    @Column(name = "title")
    private String title;

    @Column(name = "track_number")
    private int trackNumber;

    @Column(name = "track_number_total")
    private int trackNumberTotal;

    @Column(name = "disc_number")
    private int discNumber;

    @Column(name = "disc_number_total")
    private int discNumberTotal;

    @Column(name = "lyrics")
    private String lyrics;

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
