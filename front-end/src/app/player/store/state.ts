import { Song } from '../../songs/song';
import { AppState } from '../../store/state';

export interface PlaylistRootState extends AppState {
  playlist: PlaylistState;
}

export interface PlaylistState {
  playlist: Song[];
  currentSong: Song | null;
}

export const playlistInitialState: PlaylistState = {
  playlist: [],
  currentSong: null,
};
