import { Injectable } from '@angular/core';

import { Actions, createEffect, ofType } from '@ngrx/effects';

import { forkJoin } from 'rxjs';
import { map, switchMap } from 'rxjs/operators';

import { SongsService } from '../../songs/songs.service';
import { addToQueue, addToQueueSuccess } from './actions';

@Injectable()
export class QueueEffects {
  loadAlbums$ = createEffect(() =>
    this._actions$.pipe(
      ofType(addToQueue),
      switchMap(({ songIds }) =>
        forkJoin(
          songIds.map((songId) => this._songsService.getSong(songId)),
        ).pipe(map((songs) => addToQueueSuccess({ songs }))),
      ),
    ),
  );

  constructor(
    private readonly _actions$: Actions,
    private readonly _songsService: SongsService,
  ) {}
}
