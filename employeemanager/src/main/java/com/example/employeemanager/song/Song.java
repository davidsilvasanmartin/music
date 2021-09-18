package com.example.employeemanager.song;

import javax.persistence.*;
import java.io.Serializable;

@Entity
class Song implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private int id;
    private String path;
    private int albumId;
    private String title;
    private String lyrics;

    public Song() {
    }

    public Song(int id, String path, int albumId, String title, String lyrics) {
        this.id = id;
        this.path = path;
        this.albumId = albumId;
        this.title = title;
        this.lyrics = lyrics;
    }

    public Song(String path, int albumId, String title, String lyrics) {
        this.path = path;
        this.albumId = albumId;
        this.title = title;
        this.lyrics = lyrics;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getAlbumId() {
        return albumId;
    }

    public void setAlbumId(int albumId) {
        this.albumId = albumId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLyrics() {
        return lyrics;
    }

    public void setLyrics(String lyrics) {
        this.lyrics = lyrics;
    }
}
