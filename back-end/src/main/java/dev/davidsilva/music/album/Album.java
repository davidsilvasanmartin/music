package dev.davidsilva.music.album;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import dev.davidsilva.music.song.Song;
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
class Album implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private int id;
    @Column(name = "artpath")
    private String artPath;
    @Column(name = "albumartist")
    private String albumArtist;
    private String album;
    private String genre;
    private int year;
    @OneToMany(mappedBy = "album")
    @JsonManagedReference
    private List<Song> songs;

}
