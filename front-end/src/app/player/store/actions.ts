import { createAction, props } from '@ngrx/store';
import { Song } from 'src/app/songs/song';

export enum PlayerActionTypes {
  addToPlaylist = '[Player] Add to playlist',
  removeFromPlaylist = '[Player] Remove from playlist',
  next = '[Player] Next',
}

export const addToPlaylist = createAction(
  PlayerActionTypes.addToPlaylist,
  props<{ songs: Song[] }>()
);

export const next = createAction(PlayerActionTypes.next);
