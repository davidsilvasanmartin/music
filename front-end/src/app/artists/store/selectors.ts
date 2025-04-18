import { createFeatureSelector } from '@ngrx/store';

import { ArtistsState } from './state';

export const selectArtistsFeatureState =
  createFeatureSelector<ArtistsState>('artists');
