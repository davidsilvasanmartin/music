import { Directive, inject } from '@angular/core';
import { ActivatedRoute, Params } from '@angular/router';

import { BehaviorSubject, filter } from 'rxjs';

import { SearchMapperService } from '../search';
import { Sort, SortMapperService } from '../sort';
import type { PaginationSortSearchParams } from './pagination-sort-search-params';

@Directive({})
export abstract class PaginatedSortedSearchableListComponent {
  private readonly _params$ =
    new BehaviorSubject<PaginationSortSearchParams | null>(null);
  protected readonly params$ = this._params$
    .asObservable()
    .pipe(filter(Boolean));

  protected constructor(private readonly _defaultSort: Sort) {
    const activatedRoute = inject(ActivatedRoute);
    const sortMapperService = inject(SortMapperService);
    const searchMapperService = inject(SearchMapperService);

    activatedRoute.queryParams.subscribe((p: Params) => {
      this._params$.next({
        page: Number(p['page']) || 1,
        size: Number(p['size']) || 10,
        sort: p['sort']
          ? sortMapperService.toObject(p['sort'])
          : this._defaultSort,
        search: p['search'] ? searchMapperService.toObject(p['search']) : null,
      });
    });
  }
}
