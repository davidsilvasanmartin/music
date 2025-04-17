import { createAction, props } from '@ngrx/store';

import type { PageableResource } from '../../api/api-pageable-resource-request';
import type { PaginationSortSearchParams } from '../../ui/pagination-sort-search';
import type { Artist } from '../artist';

export enum ArtistsActionTypes {
  loadArtists = '[Artists] Load Artists',
  loadArtistsSuccess = '[Artists] Load Artists Success',
  loadArtistsFail = '[Artists] Load Artists Fail',
  reset = '[Artists] Reset',
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
