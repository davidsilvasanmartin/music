package dev.davidsilva.music.playlist;

import com.fasterxml.jackson.annotation.JsonInclude;
import dev.davidsilva.music.song.SongDto;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class PlaylistItemDto implements Serializable {
    private Integer id;
    private Integer position;
    private String mbTrackId;
    private String songTitle;
    private String mbAlbumId;
    private String albumTitle;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private SongDto song;
    // Will not be adding a "playlist" field to this DTO, because ItemDtos
    // are never supposed to be handled independently, by themselves.
    // The way to create/read/update/delete a PlaylistItem is through the
    // corresponding PlaylistDto.
}