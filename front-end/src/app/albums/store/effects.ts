import { Injectable } from '@angular/core';

import { Actions, createEffect, ofType } from '@ngrx/effects';
import { map, switchMap } from 'rxjs/operators';

import * as albumsActions from './actions';
import { AlbumsService } from '../albums.service';
import { Album } from '../album';

@Injectable()
export class AlbumsEffects {
  loadAlbums$ = createEffect(() =>
    this._actions$.pipe(
      ofType(albumsActions.loadAlbums),
      switchMap(() =>
        this._albumsService
          .getAlbums()
          .pipe(
            map((albums: Album[]) =>
              albumsActions.loadAlbumsSuccess({ albums })
            )
          )
      )
    )
  );

  constructor(
    private readonly _actions$: Actions,
    private readonly _albumsService: AlbumsService
  ) {}
}
