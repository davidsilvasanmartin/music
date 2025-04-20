import { createSelector } from '@ngrx/store';

import { selectPlaylistsFeatureState } from '../../store/selectors';
import type { PlaylistsListState } from './state';

const selectPlaylistsListState = createSelector(
  selectPlaylistsFeatureState,
  (state) => state.list,
);

export const selectPlaylists = createSelector(
  selectPlaylistsListState,
  (state: PlaylistsListState) => state.playlists.data ?? [],
);

export const selectTotalElements = createSelector(
  selectPlaylistsListState,
  (state: PlaylistsListState) => state.playlists.data?.length ?? 0,
);
