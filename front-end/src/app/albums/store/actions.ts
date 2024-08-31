import { PageEvent } from '@angular/material/paginator';

import { createAction, props } from '@ngrx/store';

import { PageableResource } from '../../api/api-pageable-resource-request';
import { Album } from '../album';

export enum AlbumsActionTypes {
  loadAlbums = '[Albums] Load Albums',
  loadAlbumsSuccess = '[Albums] Load Albums Success',
  loadAlbumsFail = '[Albums] Load Albums Fail',
  reset = '[Albums] Reset',
}

export const loadAlbums = createAction(
  AlbumsActionTypes.loadAlbums,
  props<{ pageEvent?: PageEvent }>()
);

export const loadAlbumsSuccess = createAction(
  AlbumsActionTypes.loadAlbumsSuccess,
  props<{ albums: PageableResource<Album[]> }>()
);

export const loadAlbumsFail = createAction(
  AlbumsActionTypes.loadAlbumsFail,
  props<{ error: any }>()
);

export const reset = createAction(AlbumsActionTypes.reset);
