import { createAction, props } from '@ngrx/store';

import type { Artist } from '../../artist';

export enum ArtistsActionTypes {
  loadArtist = '[Artists View] Load Artists',
  loadArtistSuccess = '[Artists View] Load Artist Success',
  loadArtistFail = '[Artists View] Load Artist Fail',
  reset = '[Artists View] Reset',
}

export const loadArtist = createAction(
  ArtistsActionTypes.loadArtist,
  props<{ id: number }>(),
);

export const loadArtistSuccess = createAction(
  ArtistsActionTypes.loadArtistSuccess,
  props<{ artist: Artist }>(),
);

export const loadArtistFail = createAction(
  ArtistsActionTypes.loadArtistFail,
  props<{ error: unknown }>(),
);

export const reset = createAction(ArtistsActionTypes.reset);
