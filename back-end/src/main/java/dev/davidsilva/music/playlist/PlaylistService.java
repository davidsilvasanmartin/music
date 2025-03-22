package dev.davidsilva.music.playlist;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlaylistService {
    private final PlaylistRepository playlistRepository;
    private final PlaylistDtoMapper playlistDtoMapper;

    PlaylistDto createPlaylist(PlaylistDto playlist) {
        // TODO
        return null;
    }
}
