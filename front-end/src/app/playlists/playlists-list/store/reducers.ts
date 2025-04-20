import { Action, createReducer, on } from '@ngrx/store';

import {
  loadPlaylists,
  loadPlaylistsFail,
  loadPlaylistsSuccess,
  reset,
} from './actions';
import { playlistsListInitialState, type PlaylistsListState } from './state';

const reducer = createReducer(
  playlistsListInitialState,
  on(loadPlaylists, (state) => ({
    ...state,
    playlists: { data: [], loading: true, error: null },
  })),
  on(loadPlaylistsSuccess, (state, { playlists }) => ({
    ...state,
    playlists: { data: playlists, loading: false, error: null },
  })),
  on(loadPlaylistsFail, (state, { error }) => ({
    ...state,
    playlists: { data: [], loading: false, error },
  })),
  on(reset, () => playlistsListInitialState),
);

export const playlistsListReducer = (
  state: PlaylistsListState | undefined,
  action: Action,
) => reducer(state, action);
