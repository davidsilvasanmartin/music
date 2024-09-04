import { Action, createReducer, on } from '@ngrx/store';

import { Song } from '../../songs/song';
import * as playlistActions from './actions';
import { playlistInitialState, PlaylistState } from './state';

const reducer = createReducer(
  playlistInitialState,
  on(playlistActions.addToPlaylist, (state, { songs }) => ({
    ...state,
    playlist: [...state.playlist, ...songs],
  })),
  on(playlistActions.removeFromPlaylist, (state, { song }) => ({
    ...state,
    playlist: state.playlist.filter((s: Song) => s !== song),
  })),
  on(playlistActions.next, (state) => ({
    ...state,
    playlist: state.playlist.slice(1),
  })),
  on(playlistActions.reset, () => playlistInitialState),
);

export const playlistReducer = (state: PlaylistState, action: Action) =>
  reducer(state, action);
