package dev.davidsilva.music.beets.album;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * TODO this is a copy to Album. Needs to be updated with the properties from beets
 *  that we want to transfer, for example the MusicBrainz id
 */
@Setter
@Getter
@Entity
@Table(name = "albums")
public
class BeetsAlbum implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
}
