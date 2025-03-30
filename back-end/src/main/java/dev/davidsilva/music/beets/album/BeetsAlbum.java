package dev.davidsilva.music.beets.album;

import dev.davidsilva.music.beets.item.BeetsItem;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "albums")
public
class BeetsAlbum implements Serializable {
    @Id
    @Column(nullable = false, updatable = false)
    private int id;

    @Column(name = "mb_albumid")
    private String mbAlbumId;

    @Column(name = "artpath")
    private String artPath;

    @Column(name = "albumartist")
    private String albumArtist;

    @Column(name = "album")
    private String album;

    @Column(name = "genre")
    private String genre;

    @Column(name = "original_year")
    private int year;

    @OneToMany(mappedBy = "album", fetch = FetchType.EAGER)
    private List<BeetsItem> songs;
}
