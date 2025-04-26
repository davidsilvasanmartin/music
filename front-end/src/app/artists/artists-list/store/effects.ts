import { Injectable } from '@angular/core';

import { Actions, createEffect, ofType } from '@ngrx/effects';

import { of } from 'rxjs';
import { catchError, map, switchMap } from 'rxjs/operators';

import type { PageableResource } from '../../../shared/api/api-pageable-resource-request';
import type { Artist } from '../../artist';
import { ArtistsService } from '../../artists.service';
import { loadArtists, loadArtistsFail, loadArtistsSuccess } from './actions';

@Injectable()
export class ArtistsListEffects {
  loadArtists$ = createEffect(() =>
    this._actions$.pipe(
      ofType(loadArtists),
      switchMap(({ params }) =>
        this._artistsService.getArtists(params).pipe(
          map((artists: PageableResource<Artist[]>) =>
            loadArtistsSuccess({ artists }),
          ),
          catchError((error) => of(loadArtistsFail({ error }))),
        ),
      ),
    ),
  );

  constructor(
    private readonly _actions$: Actions,
    private readonly _artistsService: ArtistsService,
  ) {}
}
