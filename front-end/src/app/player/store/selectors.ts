import { createFeatureSelector, createSelector } from '@ngrx/store';

import type { Song } from '../../songs/song';
import type { PlaylistState } from './state';

export const getPlaylistState =
  createFeatureSelector<PlaylistState>('playlist');

export const getPlaylist = createSelector(
  getPlaylistState,
  (state: PlaylistState) => state.playlist,
);

export const getCurrentSong = createSelector(
  getPlaylist,
  (playlist: Song[]) => playlist[0],
);

export const getNextSongs = createSelector(getPlaylist, (playlist: Song[]) =>
  playlist.length > 0 ? playlist.slice(1) : [],
);
