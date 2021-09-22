import { Action, createReducer, on } from '@ngrx/store';
import { Song } from 'src/app/songs/song';

import * as playerActions from './actions';
import { playerInitialState, PlaylistState } from './state';

const reducer = createReducer(
  playerInitialState,
  on(playerActions.addToPlaylist, (state, { songs }) => ({
    playlist: [...state.playlist, ...songs],
  })),
  on(playerActions.removeFromPlaylist, (state, { song }) => ({
    playlist: state.playlist.filter((s: Song) => s !== song),
  })),
  on(playerActions.next, (state) => ({
    playlist: state.playlist.slice(1),
  }))
);

export const playlistReducer = (state: PlaylistState, action: Action) =>
  reducer(state, action);
