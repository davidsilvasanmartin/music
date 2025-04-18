import { Injectable } from '@angular/core';

import { Actions, createEffect, ofType } from '@ngrx/effects';

import { throwError } from 'rxjs';
import { catchError, map, switchMap } from 'rxjs/operators';

import type { Artist } from '../../artist';
import { ArtistsService } from '../../artists.service';
import * as artistsActions from './actions';

@Injectable()
export class ArtistsViewEffects {
  loadArtist$ = createEffect(() =>
    this._actions$.pipe(
      ofType(artistsActions.loadArtist),
      switchMap(({ id }) =>
        this._artistsService.getArtist(id).pipe(
          map((artist: Artist) => artistsActions.loadArtistSuccess({ artist })),
          catchError((error) => {
            artistsActions.loadArtistFail({ error });
            return throwError(() => error);
          }),
        ),
      ),
    ),
  );

  constructor(
    private readonly _actions$: Actions,
    private readonly _artistsService: ArtistsService,
  ) {}
}
