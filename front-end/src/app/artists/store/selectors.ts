// import { createFeatureSelector, createSelector } from '@ngrx/store';
//
// import type { ArtistsState } from './state';
//
// export const getArtistsState = createFeatureSelector<ArtistsState>('artists');
//
// export const getArtists = createSelector(
//   getArtistsState,
//   (state: ArtistsState) => state.artists.data?.content || [],
// );
//
// export const getTotalElements = createSelector(
//   getArtistsState,
//   (state: ArtistsState) => state.artists.data?.totalElements || 0,
// );

import { createFeatureSelector } from '@ngrx/store';

import { ArtistsState } from './state';

export const getArtistsFeatureState =
  createFeatureSelector<ArtistsState>('artists');
