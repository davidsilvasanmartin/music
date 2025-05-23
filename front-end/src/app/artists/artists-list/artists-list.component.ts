import { Component, OnDestroy } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';

import { UntilDestroy } from '@ngneat/until-destroy';
import { select, Store } from '@ngrx/store';

import { Observable } from 'rxjs';

import { PaginatedSortedSearchableListComponent } from '../../ui/pagination-sort-search';
import { SortDirection } from '../../ui/sort';
import type { Artist } from '../artist';
import { loadArtists, reset } from './store/actions';
import { selectArtists, selectTotalElements } from './store/selectors';

@UntilDestroy()
@Component({
  selector: 'app-artists-list',
  templateUrl: './artists-list.component.html',
  styles: ':host { display: contents;}',
})
export class ArtistsListComponent
  extends PaginatedSortedSearchableListComponent
  implements OnDestroy
{
  artists$: Observable<Artist[]>;
  totalElements$: Observable<number>;

  constructor(private readonly _store: Store) {
    super({ field: 'name', direction: SortDirection.ASC });
    this.artists$ = this._store.pipe(select(selectArtists));
    this.totalElements$ = this._store.pipe(select(selectTotalElements));
    this.params$
      .pipe(takeUntilDestroyed())
      .subscribe((paginationParams) =>
        this._store.dispatch(loadArtists({ params: paginationParams })),
      );
  }

  ngOnDestroy() {
    this._store.dispatch(reset());
  }
}
