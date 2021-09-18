package com.example.employeemanager.album;

public class AlbumNotFoundException extends RuntimeException {
    public AlbumNotFoundException(String s) {
        super(s);
    }
}
