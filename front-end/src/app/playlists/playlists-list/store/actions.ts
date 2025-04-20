import { createAction, props } from '@ngrx/store';

import type { Playlist } from '../../models';

export enum PlaylistsActionTypes {
  loadPlaylists = '[Playlists List] Load Playlists',
  loadPlaylistsSuccess = '[Playlists List] Load Playlists Success',
  loadPlaylistsFail = '[Playlists List] Load Playlists Fail',
  reset = '[Playlists List] Reset',
}

export const loadPlaylists = createAction(PlaylistsActionTypes.loadPlaylists);

export const loadPlaylistsSuccess = createAction(
  PlaylistsActionTypes.loadPlaylistsSuccess,
  props<{ playlists: Playlist[] }>(),
);

export const loadPlaylistsFail = createAction(
  PlaylistsActionTypes.loadPlaylistsFail,
  props<{ error: unknown }>(),
);

export const reset = createAction(PlaylistsActionTypes.reset);
