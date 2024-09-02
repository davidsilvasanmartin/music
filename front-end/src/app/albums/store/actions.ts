import { createAction, props } from '@ngrx/store';

import { PageableResource } from '../../api/api-pageable-resource-request';
import { PaginationParams } from '../../ui/pagination/pagination-params';
import { Album } from '../album';

export enum AlbumsActionTypes {
  loadAlbums = '[Albums] Load Albums',
  loadAlbumsSuccess = '[Albums] Load Albums Success',
  loadAlbumsFail = '[Albums] Load Albums Fail',
  reset = '[Albums] Reset',
}

export const loadAlbums = createAction(
  AlbumsActionTypes.loadAlbums,
  props<{ paginationParams: PaginationParams }>(),
);

export const loadAlbumsSuccess = createAction(
  AlbumsActionTypes.loadAlbumsSuccess,
  props<{ albums: PageableResource<Album[]> }>(),
);

export const loadAlbumsFail = createAction(
  AlbumsActionTypes.loadAlbumsFail,
  props<{ error: any }>(),
);

export const reset = createAction(AlbumsActionTypes.reset);
