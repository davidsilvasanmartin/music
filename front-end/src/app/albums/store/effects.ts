import { Injectable } from '@angular/core';

import { Actions, createEffect, ofType } from '@ngrx/effects';
import { map, switchMap } from 'rxjs/operators';

import { PageableResource } from '../../api/api-pageable-resource-request';
import { AlbumsService } from '../albums.service';
import { Album } from '../album';
import * as albumsActions from './actions';

@Injectable()
export class AlbumsEffects {
  loadAlbums$ = createEffect(() =>
    this._actions$.pipe(
      ofType(albumsActions.loadAlbums),
      switchMap(({ pageEvent }) =>
        this._albumsService
          .getAlbums(pageEvent)
          .pipe(
            map((albums: PageableResource<Album[]>) =>
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
