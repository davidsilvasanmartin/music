import { Action, createReducer, on } from '@ngrx/store';

import {
  loadPlaylist,
  loadPlaylistFail,
  loadPlaylistSuccess,
  reset,
} from './actions';
import { playlistsViewInitialState, type PlaylistsViewState } from './state';

const reducer = createReducer(
  playlistsViewInitialState,
  on(
    loadPlaylist,
    (state): PlaylistsViewState => ({
      ...state,
      playlist: { data: null, loading: true, error: null },
    }),
  ),
  on(
    loadPlaylistSuccess,
    (state, { playlist }): PlaylistsViewState => ({
      ...state,
      playlist: { data: playlist, loading: false, error: null },
    }),
  ),
  on(
    loadPlaylistFail,
    (state, { error }): PlaylistsViewState => ({
      ...state,
      playlist: { data: null, loading: false, error },
    }),
  ),
  on(reset, (): PlaylistsViewState => playlistsViewInitialState),
);

export const playlistsViewReducer = (
  state: PlaylistsViewState | undefined,
  action: Action,
) => reducer(state, action);
