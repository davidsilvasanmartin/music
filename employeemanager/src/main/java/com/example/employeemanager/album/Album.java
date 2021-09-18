package com.example.employeemanager.album;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(schema = "albums")
class Album implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private int id;
    private String artPath;
    private String albumArtist;
    private String album;
    private String genre;
    private int year;

    public Album() {
    }

    public Album(int id, String artPath, String albumArtist, String album, String genre,
                 int year) {
        this.id = id;
        this.artPath = artPath;
        this.albumArtist = albumArtist;
        this.album = album;
        this.genre = genre;
        this.year = year;
    }

    public Album(String artPath, String albumArtist, String album, String genre,
                 int year) {
        this.artPath = artPath;
        this.albumArtist = albumArtist;
        this.album = album;
        this.genre = genre;
        this.year = year;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getArtPath() {
        return artPath;
    }

    public void setArtPath(String artPath) {
        this.artPath = artPath;
    }

    public String getAlbumArtist() {
        return albumArtist;
    }

    public void setAlbumArtist(String albumArtist) {
        this.albumArtist = albumArtist;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String jobTitle) {
        this.album = jobTitle;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
