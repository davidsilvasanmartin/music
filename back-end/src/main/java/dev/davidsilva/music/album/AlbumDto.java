package dev.davidsilva.music.album;

import lombok.Data;

import java.util.List;

@Data
public class AlbumDto {
    private int id;

    private String albumArtist;

    private String album;

    private List<String> genres;

    private int year;
}
