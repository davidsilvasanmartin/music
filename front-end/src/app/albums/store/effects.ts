import { Injectable } from '@angular/core';

import { Actions, createEffect, ofType } from '@ngrx/effects';

import { map, switchMap } from 'rxjs/operators';

import type { PageableResource } from '../../api/api-pageable-resource-request';
import type { Album } from '../album';
import { AlbumsService } from '../albums.service';
import * as albumsActions from './actions';

@Injectable()
export class AlbumsEffects {
  loadAlbums$ = createEffect(() =>
    this._actions$.pipe(
      ofType(albumsActions.loadAlbums),
      switchMap(({ params }) =>
        this._albumsService
          .getAlbums(params)
          .pipe(
            map((albums: PageableResource<Album[]>) =>
              albumsActions.loadAlbumsSuccess({ albums }),
            ),
          ),
      ),
    ),
  );

  constructor(
    private readonly _actions$: Actions,
    private readonly _albumsService: AlbumsService,
  ) {}
}
