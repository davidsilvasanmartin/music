import { Injectable } from '@angular/core';

import { Actions, createEffect, ofType } from '@ngrx/effects';

import { forkJoin } from 'rxjs';
import { map, switchMap } from 'rxjs/operators';

import { SongsService } from '../../songs/songs.service';
import * as playerActions from './actions';

@Injectable()
export class PlayerEffects {
  loadAlbums$ = createEffect(() =>
    this._actions$.pipe(
      ofType(playerActions.addToPlaylist),
      switchMap(({ songIds }) =>
        forkJoin(
          songIds.map((songId) => this._songsService.getSong(songId)),
        ).pipe(map((songs) => playerActions.addToPlaylistSuccess({ songs }))),
      ),
    ),
  );

  constructor(
    private readonly _actions$: Actions,
    private readonly _songsService: SongsService,
  ) {}
}
