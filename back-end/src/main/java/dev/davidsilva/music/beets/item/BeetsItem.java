package dev.davidsilva.music.beets.item;

import dev.davidsilva.music.beets.album.BeetsAlbum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
@Entity
@Table(name = "items")
public class BeetsItem implements Serializable {
    @Id
    @Column(nullable = false, updatable = false)
    private int id;

    private String path;

    @ManyToOne()
    @JoinColumn(name = "album_id", referencedColumnName = "id")
    private BeetsAlbum album;

    private String title;

    private String lyrics;
}
