package dev.davidsilva.music.playlist;

import dev.davidsilva.music.exception.EntityNotFoundException;
import dev.davidsilva.music.security.auth.AuthService;
import dev.davidsilva.music.song.Song;
import dev.davidsilva.music.song.SongDto;
import dev.davidsilva.music.song.SongDtoMapper;
import dev.davidsilva.music.song.SongRepository;
import dev.davidsilva.music.utils.DtoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
@RequiredArgsConstructor
public class PlaylistDtoMapper implements DtoMapper<PlaylistDto, Playlist> {
    private final SongDtoMapper songDtoMapper;
    private final SongRepository songRepository;
    private final AuthService authService;

    @Override
    public Playlist toEntity(PlaylistDto playlistDto) {
        ArrayList<Song> songs = new ArrayList<>();
        for (SongDto songDto : playlistDto.getSongs()) {
            songs.add(songRepository.findById(songDto.getId()).orElseThrow(
                    () -> new EntityNotFoundException(Song.class.getName(), songDto.getId())
            ));
        }

        Playlist playlist = new Playlist();
        playlist.setName(playlistDto.getName());
        playlist.setUser(authService.getLoggedInUser());

        return playlist;
    }

    @Override
    public PlaylistDto toDto(Playlist entity) {
        return null;
    }
}
