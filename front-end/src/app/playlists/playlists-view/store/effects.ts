import { Injectable } from '@angular/core';

import { Actions, createEffect, ofType } from '@ngrx/effects';

import { of } from 'rxjs';
import { catchError, map, switchMap } from 'rxjs/operators';

import type { Playlist } from '../../models';
import { PlaylistsService } from '../../playlists.service';
import { loadPlaylist, loadPlaylistFail, loadPlaylistSuccess } from './actions';

@Injectable()
export class PlaylistsViewEffects {
  loadPlaylist$ = createEffect(() =>
    this._actions$.pipe(
      ofType(loadPlaylist),
      switchMap(({ id }) =>
        this._playlistsService.getPlaylist(id).pipe(
          map((playlist: Playlist) => loadPlaylistSuccess({ playlist })),
          catchError((error) => of(loadPlaylistFail({ error }))),
        ),
      ),
    ),
  );

  constructor(
    private readonly _actions$: Actions,
    private readonly _playlistsService: PlaylistsService,
  ) {}
}
