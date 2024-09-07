import { Component, OnDestroy } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { ActivatedRoute } from '@angular/router';

import { UntilDestroy } from '@ngneat/until-destroy';
import { select, Store } from '@ngrx/store';

import { Observable } from 'rxjs';

import { PaginatedListComponent } from '../ui/pagination/paginated-list/paginated-list.component';
import { Album } from './album';
import * as albumsActions from './store/actions';
import * as albumsSelectors from './store/selectors';

@UntilDestroy()
@Component({
  selector: 'app-albums',
  templateUrl: './albums.component.html',
  styleUrls: ['./albums.component.scss'],
})
export class AlbumsComponent
  extends PaginatedListComponent
  implements OnDestroy
{
  albums$: Observable<Album[]>;
  totalElements$: Observable<number>;

  constructor(
    activatedRoute: ActivatedRoute,
    private readonly _store: Store,
  ) {
    super(activatedRoute);
    this.albums$ = this._store.pipe(select(albumsSelectors.getAlbums));
    this.totalElements$ = this._store.pipe(
      select(albumsSelectors.getTotalElements),
    );
    this.paginationParams$
      .pipe(takeUntilDestroyed())
      .subscribe((paginationParams) =>
        this._store.dispatch(albumsActions.loadAlbums({ paginationParams })),
      );
  }

  ngOnDestroy() {
    this._store.dispatch(albumsActions.reset());
  }
}
