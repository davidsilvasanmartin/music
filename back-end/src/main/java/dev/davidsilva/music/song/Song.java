package dev.davidsilva.music.song;

import dev.davidsilva.music.album.Album;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
@Entity
@Table(name = "items")
public
class Song implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private int id;

    private String path;

    @ManyToOne()
    @JoinColumn(
            name = "album_id",
            referencedColumnName = "id",
            insertable = false,
            updatable = false
    )
    private Album album;

    private String title;

    private String lyrics;
}
