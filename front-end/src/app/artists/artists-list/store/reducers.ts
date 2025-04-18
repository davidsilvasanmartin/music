import { Action, createReducer, on } from '@ngrx/store';

import * as artistsActions from './actions';
import { artistsInitialState, type ArtistsListState } from './state';

const reducer = createReducer(
  artistsInitialState,
  on(artistsActions.loadArtists, (state) => ({
    ...state,
    artists: {
      data: null,
      loading: true,
      error: null,
    },
  })),
  on(artistsActions.loadArtistsSuccess, (state, { artists }) => ({
    ...state,
    artists: {
      data: artists,
      loading: false,
      error: null,
    },
  })),
  on(artistsActions.loadArtistsFail, (state, { error }) => ({
    ...state,
    artists: {
      data: null,
      loading: false,
      error,
    },
  })),
  on(artistsActions.reset, () => artistsInitialState),
);

export const artistsListReducer = (
  state: ArtistsListState | undefined,
  action: Action,
) => reducer(state, action);
