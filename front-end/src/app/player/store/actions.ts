import { createAction, props } from "@ngrx/store";

import { Song } from "../../songs/song";

export enum PlaylistActionTypes {
  addToPlaylist = '[Playlist] Add to playlist',
  addToPlaylistSuccess = '[Playlist] Add to playlist Success',
  removeFromPlaylist = '[Playlist] Remove from playlist',
  next = '[Playlist] Next',
  reset = '[Playlist] Reset',
}

export const addToPlaylist = createAction(
  PlaylistActionTypes.addToPlaylist,
  props<{ songIds: number[] }>(),
);

export const addToPlaylistSuccess = createAction(
  PlaylistActionTypes.addToPlaylistSuccess,
  props<{ songs: Song[] }>(),
);

export const removeFromPlaylist = createAction(
  PlaylistActionTypes.removeFromPlaylist,
  props<{ songIndex: number }>),
);

export const next = createAction(PlaylistActionTypes.next);

export const reset = createAction(PlaylistActionTypes.reset);
