import { Injectable } from '@angular/core';

import { Actions, createEffect, ofType } from '@ngrx/effects';

import { of } from 'rxjs';
import { catchError, map, switchMap } from 'rxjs/operators';

import type { Playlist } from '../../models';
import { PlaylistsService } from '../../playlists.service';
import {
  loadPlaylists,
  loadPlaylistsFail,
  loadPlaylistsSuccess,
} from './actions';

@Injectable()
export class PlaylistsListEffects {
  loadPlaylists$ = createEffect(() =>
    this._actions$.pipe(
      ofType(loadPlaylists),
      switchMap(() =>
        this._playlistsService.getPlaylists().pipe(
          map((playlists: Playlist[]) => loadPlaylistsSuccess({ playlists })),
          catchError((error) => of(loadPlaylistsFail({ error }))),
        ),
      ),
    ),
  );

  constructor(
    private readonly _actions$: Actions,
    private readonly _playlistsService: PlaylistsService,
  ) {}
}
