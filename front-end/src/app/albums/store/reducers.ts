import { Action, createReducer, on } from '@ngrx/store';

import * as albumsActions from './actions';
import { albumsInitialState, type AlbumsState } from './state';

const reducer = createReducer(
  albumsInitialState,
  on(albumsActions.loadAlbums, (state) => ({
    ...state,
    albums: {
      data: null,
      loading: true,
      error: null,
    },
  })),
  on(albumsActions.loadAlbumsSuccess, (state, { albums }) => ({
    ...state,
    albums: {
      data: albums,
      loading: false,
      error: null,
    },
  })),
  on(albumsActions.loadAlbumsFail, (state, { error }) => ({
    ...state,
    albums: {
      data: null,
      loading: false,
      error,
    },
  })),
  on(albumsActions.reset, () => albumsInitialState),
);

export const albumsReducer = (state: AlbumsState, action: Action) =>
  reducer(state, action);
