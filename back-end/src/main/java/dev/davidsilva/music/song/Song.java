package dev.davidsilva.music.song;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

// TODO this "items" is causing trouble
@Setter
@Getter
//@Entity
//@Table(name = "items")
public class Song implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private int id;

    private String path;

//    @ManyToOne()
//    @JoinColumn(
//            name = "album_id",
//            referencedColumnName = "id",
//            insertable = false,
//            updatable = false
//    )
//    private Album album;

    private String title;

    private String lyrics;
}
