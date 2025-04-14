package dev.davidsilva.music.genre;

import com.fasterxml.jackson.annotation.JsonInclude;
import dev.davidsilva.music.album.AlbumDto;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
@Data
public class GenreDto implements Serializable {
    private int id;
    private String name;
    private List<AlbumDto> albums;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
