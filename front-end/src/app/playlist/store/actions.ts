import { createAction, props } from '@ngrx/store';
import { Song } from 'src/app/songs/song';

export enum PlaylistActionTypes {
  addToPlaylist = '[Playlist] Add to playlist',
  removeFromPlaylist = '[Playlist] Remove from playlist',
  next = '[Playlist] Next',
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
