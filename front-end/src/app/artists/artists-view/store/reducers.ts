import { Action, createReducer, on } from '@ngrx/store';

import {
  loadArtist,
  loadArtistFail,
  loadArtistSuccess,
  reset,
} from './actions';
import { artistsViewInitialState, type ArtistsViewState } from './state';

const reducer = createReducer(
  artistsViewInitialState,
  on(
    loadArtist,
    (state): ArtistsViewState => ({
      ...state,
      artist: { data: null, loading: true, error: null },
    }),
  ),
  on(
    loadArtistSuccess,
    (state, { artist }): ArtistsViewState => ({
      ...state,
      artist: { data: artist, loading: false, error: null },
    }),
  ),
  on(
    loadArtistFail,
    (state, { error }): ArtistsViewState => ({
      ...state,
      artist: { data: null, loading: false, error },
    }),
  ),
  on(reset, (): ArtistsViewState => artistsViewInitialState),
);

export const artistsViewReducer = (
  state: ArtistsViewState | undefined,
  action: Action,
) => reducer(state, action);
