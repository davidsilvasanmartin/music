import { ChangeDetectionStrategy, Component, OnInit } from '@angular/core';
import { ActivatedRoute, Params } from '@angular/router';

import { UntilDestroy, untilDestroyed } from '@ngneat/until-destroy';
import { Store } from '@ngrx/store';

import { filter } from 'rxjs';
import { map } from 'rxjs/operators';

import { loadPlaylist } from './store/actions';
import { selectPlaylist } from './store/selectors';

@UntilDestroy()
@Component({
  selector: 'app-playlists-view',
  templateUrl: './playlists-view.component.html',
  styles: ':host { display: contents; }',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class PlaylistsViewComponent implements OnInit {
  playlist = this._store.selectSignal(selectPlaylist);

  constructor(
    private readonly _store: Store,
    private readonly _route: ActivatedRoute,
  ) {}

  ngOnInit() {
    this._route.params
      .pipe(
        map((params: Params) => params['playlistId']),
        filter((playlistId): playlistId is string => !!playlistId),
        map((playlistId: string) => parseInt(playlistId, 10)),
        filter((playlistId: number) => !isNaN(playlistId)),
        untilDestroyed(this),
      )
      .subscribe((id: number) => {
        this._store.dispatch(loadPlaylist({ id }));
      });
  }
}
