import { Injectable } from '@angular/core';

import { Actions, createEffect, ofType } from '@ngrx/effects';

import { of } from 'rxjs';
import { catchError, map, switchMap } from 'rxjs/operators';

import type { Artist } from '../../artist';
import { ArtistsService } from '../../artists.service';
import { loadArtist, loadArtistFail, loadArtistSuccess } from './actions';

@Injectable()
export class ArtistsViewEffects {
  loadArtist$ = createEffect(() =>
    this._actions$.pipe(
      ofType(loadArtist),
      switchMap(({ id }) =>
        this._artistsService.getArtist(id).pipe(
          map((artist: Artist) => loadArtistSuccess({ artist })),
          catchError((error) => of(loadArtistFail({ error }))),
        ),
      ),
    ),
  );

  constructor(
    private readonly _actions$: Actions,
    private readonly _artistsService: ArtistsService,
  ) {}
}
