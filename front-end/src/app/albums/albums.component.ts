import { Component, OnDestroy } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';

import { UntilDestroy } from '@ngneat/until-destroy';
import { select, Store } from '@ngrx/store';

import { Observable } from 'rxjs';

import { PaginatedSortedSearchableListComponent } from '../ui/pagination-sort-search';
import { SortDirection } from '../ui/sort';
import type { Album } from './album';
import * as albumsActions from './store/actions';
import * as albumsSelectors from './store/selectors';

@UntilDestroy()
@Component({
  selector: 'app-albums',
  templateUrl: './albums.component.html',
  styleUrls: ['./albums.component.scss'],
  styles: ':host { display: contents;}',
})
export class AlbumsComponent
  extends PaginatedSortedSearchableListComponent
  implements OnDestroy
{
  albums$: Observable<Album[]>;
  totalElements$: Observable<number>;

  constructor(private readonly _store: Store) {
    super({ field: 'albumArtist', direction: SortDirection.ASC });
    this.albums$ = this._store.pipe(select(albumsSelectors.getAlbums));
    this.totalElements$ = this._store.pipe(
      select(albumsSelectors.getTotalElements),
    );
    this.params$
      .pipe(takeUntilDestroyed())
      .subscribe((paginationParams) =>
        this._store.dispatch(
          albumsActions.loadAlbums({ params: paginationParams }),
        ),
      );
  }

  ngOnDestroy() {
    this._store.dispatch(albumsActions.reset());
  }
}
