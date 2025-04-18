import { createSelector } from '@ngrx/store';

import { selectArtistsFeatureState } from '../../store/selectors';
import type { ArtistsListState } from './state';

const selectArtistsListState = createSelector(
  selectArtistsFeatureState,
  (state) => state.list,
);

export const selectArtists = createSelector(
  selectArtistsListState,
  (state: ArtistsListState) => state.artists.data?.content ?? [],
);

export const selectTotalElements = createSelector(
  selectArtistsListState,
  (state: ArtistsListState) => state.artists.data?.totalElements ?? 0,
);
