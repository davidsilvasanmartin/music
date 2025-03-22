package dev.davidsilva.music.playlist;

import dev.davidsilva.music.song.SongDto;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class PlaylistDto {
    private int id;
    private String name;
    private List<SongDto> songs;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
