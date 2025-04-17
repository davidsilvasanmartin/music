import { Injectable } from '@angular/core';

import { Actions, createEffect, ofType } from '@ngrx/effects';

import { map, switchMap } from 'rxjs/operators';

import { PageableResource } from '../../api/api-pageable-resource-request';
import { Artist } from '../artist';
import { ArtistsService } from '../artists.service';
import * as artistsActions from './actions';

@Injectable()
export class ArtistsEffects {
  loadArtists$ = createEffect(() =>
    this._actions$.pipe(
      ofType(artistsActions.loadArtists),
      switchMap(({ params }) =>
        this._artistsService
          .getArtists(params)
          .pipe(
            map((artists: PageableResource<Artist[]>) =>
              artistsActions.loadArtistsSuccess({ artists }),
            ),
          ),
      ),
    ),
  );

  constructor(
    private readonly _actions$: Actions,
    private readonly _artistsService: ArtistsService,
  ) {}
}
