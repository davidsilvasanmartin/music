package com.example.music.song;

public class SongNotFoundException extends RuntimeException {
    public SongNotFoundException(String s) {
        super(s);
    }
}
