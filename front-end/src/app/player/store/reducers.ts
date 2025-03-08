import { Action, createReducer, on } from '@ngrx/store';

import * as playlistActions from './actions';
import { playlistInitialState, type PlaylistState } from './state';

const reducer = createReducer(
  playlistInitialState,
  on(playlistActions.addToPlaylistSuccess, (state, { songs }) => ({
    ...state,
    playlist: [...state.playlist, ...songs],
  })),
  on(playlistActions.removeFromPlaylist, (state, { songIndex }) => ({
    ...state,
    playlist: state.playlist
      .slice(0, songIndex)
      .concat(state.playlist.slice(songIndex + 1)),
  })),
  on(playlistActions.next, (state) => ({
    ...state,
    playlist: state.playlist.slice(1),
  })),
  on(playlistActions.reset, () => playlistInitialState),
);

export const playlistReducer = (state: PlaylistState, action: Action) =>
  reducer(state, action);
