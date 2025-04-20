import { createAction, props } from '@ngrx/store';

import type { Playlist } from '../../models';

export enum PlaylistsActionTypes {
  loadPlaylist = '[Playlists View] Load Playlists',
  loadPlaylistSuccess = '[Playlists View] Load Playlist Success',
  loadPlaylistFail = '[Playlists View] Load Playlist Fail',
  reset = '[Playlists View] Reset',
}

export const loadPlaylist = createAction(
  PlaylistsActionTypes.loadPlaylist,
  props<{ id: number }>(),
);

export const loadPlaylistSuccess = createAction(
  PlaylistsActionTypes.loadPlaylistSuccess,
  props<{ playlist: Playlist }>(),
);

export const loadPlaylistFail = createAction(
  PlaylistsActionTypes.loadPlaylistFail,
  props<{ error: unknown }>(),
);

export const reset = createAction(PlaylistsActionTypes.reset);
