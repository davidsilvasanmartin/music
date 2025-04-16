package dev.davidsilva.music.artist;

import dev.davidsilva.music.album.Album;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "artists")
public class Artist implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private int id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "mb_artist_id")
    private String mbArtistId;

    @OneToMany(mappedBy = "artist", fetch = FetchType.LAZY)
    @OrderBy("year DESC")
    private List<Album> albums;

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