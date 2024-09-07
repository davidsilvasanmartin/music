import { Component, OnDestroy } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';

import { UntilDestroy } from '@ngneat/until-destroy';
import { select, Store } from '@ngrx/store';

import { Observable } from 'rxjs';

import { PaginatedSortedFilteredListComponent } from '../ui/pagination-sort-filter/paginated-sorted-filtered-list.component';
import { SortDirection } from '../ui/sort/sort';
import { Album } from './album';
import { AlbumDto } from './album-dto';
import * as albumsActions from './store/actions';
import * as albumsSelectors from './store/selectors';

@UntilDestroy()
@Component({
  selector: 'app-albums',
  templateUrl: './albums.component.html',
  styleUrls: ['./albums.component.scss'],
})
export class AlbumsComponent
  extends PaginatedSortedFilteredListComponent<AlbumDto>
  implements OnDestroy
{
  albums$: Observable<Album[]>;
  totalElements$: Observable<number>;

  constructor(private readonly _store: Store) {
    super(['album', 'albumArtist', 'year'], {
      field: 'albumArtist',
      direction: SortDirection.ASC,
    });
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
