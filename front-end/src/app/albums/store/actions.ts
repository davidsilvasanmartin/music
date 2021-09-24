import { createAction, props } from '@ngrx/store';

import { Album } from '../album';

export enum AlbumsActionTypes {
  loadAlbums = '[Albums] Load Albums',
  loadAlbumsSuccess = '[Albums] Load Albums Success',
  loadAlbumsFail = '[Albums] Load Albums Fail',
  reset = '[Albums] Reset',
}

// TODO pagination
export const loadAlbums = createAction(AlbumsActionTypes.loadAlbums);

export const loadAlbumsSuccess = createAction(
  AlbumsActionTypes.loadAlbumsSuccess,
  props<{ albums: Album[] }>()
);

export const loadAlbumsFail = createAction(
  AlbumsActionTypes.loadAlbumsFail,
  props<{ error: any }>()
);

export const reset = createAction(AlbumsActionTypes.reset);
