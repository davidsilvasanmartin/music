import { Song } from '../../songs/song';
import { AppState } from '../../store/state';

export interface PlaylistRootState extends AppState {
  player: PlaylistState;
}

export interface PlaylistState {
  playlist: Song[];
}

export const playerInitialState: PlaylistState = {
  playlist: [],
};
