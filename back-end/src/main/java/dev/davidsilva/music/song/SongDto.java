package dev.davidsilva.music.song;

import com.fasterxml.jackson.annotation.JsonInclude;
import dev.davidsilva.music.album.AlbumDto;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
@Data
public class SongDto {
    private int id;

    private AlbumDto album;

    private String title;

    private String lyrics;
}
