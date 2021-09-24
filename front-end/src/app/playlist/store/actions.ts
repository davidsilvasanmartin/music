import { createAction, props } from '@ngrx/store';

import { Song } from '../../songs/song';

export enum PlaylistActionTypes {
  addToPlaylist = '[Playlist] Add to playlist',
  removeFromPlaylist = '[Playlist] Remove from playlist',
  next = '[Playlist] Next',
  reset = '[Playlist] Reset',
}

export const addToPlaylist = createAction(
  PlaylistActionTypes.addToPlaylist,
  props<{ songs: Song[] }>()
);

export const removeFromPlaylist = createAction(
  PlaylistActionTypes.removeFromPlaylist,
  props<{ song: Song }>()
);

export const next = createAction(PlaylistActionTypes.next);

export const reset = createAction(PlaylistActionTypes.reset);
