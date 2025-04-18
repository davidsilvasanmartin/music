import { createSelector } from '@ngrx/store';

import { selectArtistsFeatureState } from '../../store/selectors';
import type { ArtistsViewState } from './state';

const selectArtistsViewState = createSelector(
  selectArtistsFeatureState,
  (state) => state.view,
);

export const selectArtist = createSelector(
  selectArtistsViewState,
  (state: ArtistsViewState) => state.artist.data,
);
