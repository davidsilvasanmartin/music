import { createFeatureSelector } from '@ngrx/store';

import type { ArtistsState } from './state';

export const selectArtistsFeatureState =
  createFeatureSelector<ArtistsState>('artists');
