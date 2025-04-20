import { createFeatureSelector } from '@ngrx/store';

import type { PlaylistsState } from './state';

export const selectPlaylistsFeatureState =
  createFeatureSelector<PlaylistsState>('playlists');
