import { Action, createReducer, on } from '@ngrx/store';

import {
  loadArtists,
  loadArtistsFail,
  loadArtistsSuccess,
  reset,
} from './actions';
import { artistsListInitialState, type ArtistsListState } from './state';

const reducer = createReducer(
  artistsListInitialState,
  on(loadArtists, (state) => ({
    ...state,
    artists: { data: null, loading: true, error: null },
  })),
  on(loadArtistsSuccess, (state, { artists }) => ({
    ...state,
    artists: { data: artists, loading: false, error: null },
  })),
  on(loadArtistsFail, (state, { error }) => ({
    ...state,
    artists: { data: null, loading: false, error },
  })),
  on(reset, () => artistsListInitialState),
);

export const artistsListReducer = (
  state: ArtistsListState | undefined,
  action: Action,
) => reducer(state, action);
