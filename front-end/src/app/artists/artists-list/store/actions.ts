import { createAction, props } from '@ngrx/store';

import type { PageableResource } from '../../../shared/api/api-pageable-resource-request';
import type { PaginationSortSearchParams } from '../../../ui/pagination-sort-search';
import type { Artist } from '../../artist';

export enum ArtistsActionTypes {
  loadArtists = '[Artists List] Load Artists',
  loadArtistsSuccess = '[Artists List] Load Artists Success',
  loadArtistsFail = '[Artists List] Load Artists Fail',
  reset = '[Artists List] Reset',
}

export const loadArtists = createAction(
  ArtistsActionTypes.loadArtists,
  props<{ params: PaginationSortSearchParams }>(),
);

export const loadArtistsSuccess = createAction(
  ArtistsActionTypes.loadArtistsSuccess,
  props<{ artists: PageableResource<Artist[]> }>(),
);

export const loadArtistsFail = createAction(
  ArtistsActionTypes.loadArtistsFail,
  props<{ error: unknown }>(),
);

export const reset = createAction(ArtistsActionTypes.reset);
