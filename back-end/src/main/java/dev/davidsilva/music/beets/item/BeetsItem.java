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

    @Column(name = "mb_trackid")
    private String mbTrackId;

    @Column(name = "path")
    private String path;

    @ManyToOne()
    @JoinColumn(name = "album_id", referencedColumnName = "id")
    private BeetsAlbum album;

    @Column(name = "title")
    private String title;

    @Column(name = "track")
    private int trackNumber;

    @Column(name = "tracktotal")
    private int trackNumberTotal;

    @Column(name = "disc")
    private int discNumber;

    @Column(name = "disctotal")
    private int discNumberTotal;

    @Column(name = "lyrics")
    private String lyrics;
}
