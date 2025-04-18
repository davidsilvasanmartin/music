import { Action, createReducer, on } from '@ngrx/store';

import * as artistsActions from './actions';
import { artistsViewInitialState, type ArtistsViewState } from './state';

const reducer = createReducer(
  artistsViewInitialState,
  on(
    artistsActions.loadArtist,
    (state): ArtistsViewState => ({
      ...state,
      artist: {
        data: null,
        loading: true,
        error: null,
      },
    }),
  ),
  on(
    artistsActions.loadArtistSuccess,
    (state, { artist }): ArtistsViewState => ({
      ...state,
      artist: {
        data: artist,
        loading: false,
        error: null,
      },
    }),
  ),
  on(
    artistsActions.loadArtistFail,
    (state, { error }): ArtistsViewState => ({
      ...state,
      artist: {
        data: null,
        loading: false,
        error,
      },
    }),
  ),
  on(artistsActions.reset, (): ArtistsViewState => artistsViewInitialState),
);

export const artistsViewReducer = (
  state: ArtistsViewState | undefined,
  action: Action,
) => reducer(state, action);
