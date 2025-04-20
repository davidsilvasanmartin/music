import { createSelector } from '@ngrx/store';

import { selectPlaylistsFeatureState } from '../../store/selectors';
import type { PlaylistsViewState } from './state';

const selectPlaylistsViewState = createSelector(
  selectPlaylistsFeatureState,
  (state) => state.view,
);

export const selectPlaylist = createSelector(
  selectPlaylistsViewState,
  (state: PlaylistsViewState) => state.playlist.data,
);
