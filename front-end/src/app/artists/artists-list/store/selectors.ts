import { createSelector } from '@ngrx/store';

import { getArtistsFeatureState } from '../../store/selectors';
import type { ArtistsListState } from './state';

const getArtistsListState = createSelector(
  getArtistsFeatureState,
  (state) => state.list,
);

export const getArtists = createSelector(
  getArtistsListState,
  (state: ArtistsListState) => state.artists.data?.content ?? [],
);

export const getTotalElements = createSelector(
  getArtistsListState,
  (state: ArtistsListState) => state.artists.data?.totalElements ?? 0,
);
