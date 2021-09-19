import { Song } from '../../songs/song';
import { AppState } from '../../store/state';

export interface PlayerRootState extends AppState {
  player: PlayerState;
}

export interface PlayerState {
  playlist: Song[];
}

export const playerInitialState: PlayerState = {
  playlist: [],
};
