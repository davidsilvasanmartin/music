import { Component, OnDestroy, OnInit } from '@angular/core';

import { select, Store } from '@ngrx/store';
import { Observable } from 'rxjs';

import { environment } from '../../environments/environment';
import * as albumsSelectors from './store/selectors';
import * as albumsActions from './store/actions';
import { Album } from './album';
import { PageEvent } from '@angular/material/paginator';

@Component({
  selector: 'app-albums',
  templateUrl: './albums.component.html',
  styleUrls: ['./albums.component.scss'],
})
export class AlbumsComponent implements OnInit, OnDestroy {
  albums$: Observable<Album[]>;
  pageIndex$: Observable<number>;
  size$: Observable<number>;
  totalElements$: Observable<number>;

  apiUrl = environment.apiUrl;

  constructor(private readonly _store: Store) {
    this.albums$ = this._store.pipe(select(albumsSelectors.getAlbums));
    this.pageIndex$ = this._store.pipe(select(albumsSelectors.getPageIndex));
    this.size$ = this._store.pipe(select(albumsSelectors.getSize));
    this.totalElements$ = this._store.pipe(
      select(albumsSelectors.getTotalElements)
    );
  }

  ngOnInit() {
    this._store.dispatch(albumsActions.loadAlbums({}));
  }

  ngOnDestroy() {
    this._store.dispatch(albumsActions.reset());
  }

  paginateList(pageEvent: PageEvent) {
    this._store.dispatch(albumsActions.loadAlbums({ pageEvent }));
  }
}
