import { Component, OnDestroy, OnInit } from '@angular/core';

import { UntilDestroy } from '@ngneat/until-destroy';
import { select, Store } from '@ngrx/store';

import { Observable } from 'rxjs';

import type { Playlist } from '../models';
import { loadPlaylists, reset } from './store/actions';
import { selectPlaylists, selectTotalElements } from './store/selectors';

@UntilDestroy()
@Component({
  selector: 'app-playlists-list',
  templateUrl: './playlists-list.component.html',
  styles: ':host { display: contents;}',
})
export class PlaylistsListComponent implements OnInit, OnDestroy {
  playlists$: Observable<Playlist[]>;
  totalElements$: Observable<number>;

  constructor(private readonly _store: Store) {
    this.playlists$ = this._store.pipe(select(selectPlaylists));
    this.totalElements$ = this._store.pipe(select(selectTotalElements));
  }

  ngOnInit() {
    this._store.dispatch(loadPlaylists());
  }

  ngOnDestroy() {
    this._store.dispatch(reset());
  }
}
