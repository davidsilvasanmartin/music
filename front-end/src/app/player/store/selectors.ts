import { createFeatureSelector, createSelector } from '@ngrx/store';

import { Song } from '../../songs/song';
import { PlayerState } from './state';

export const getPlayerState = createFeatureSelector<PlayerState>('player');

export const getPlaylist = createSelector(
  getPlayerState,
  (state: PlayerState) => state.playlist
);

export const getCurrentSong = createSelector(
  getPlaylist,
  (playlist: Song[]) => playlist[0]
);

export const getNextSongs = createSelector(getPlaylist, (playlist: Song[]) =>
  playlist.length > 0 ? playlist.slice(1) : []
);
