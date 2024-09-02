import { Directive } from '@angular/core';
import { ActivatedRoute, Params } from '@angular/router';

import { BehaviorSubject, filter } from 'rxjs';

import { PaginationParams } from '../pagination-params';

@Directive({})
export abstract class PaginatedListComponent {
  // TODO see if this emits null on initialization or not
  private readonly _paginationParams$ =
    new BehaviorSubject<PaginationParams | null>(null);
  protected readonly paginationParams$ = this._paginationParams$
    .asObservable()
    .pipe(filter(Boolean));

  constructor(private readonly _activatedRoute: ActivatedRoute) {
    this._activatedRoute.queryParams.subscribe((p: Params) => {
      this._paginationParams$.next({
        page: Number(p['page']) || 1,
        size: Number(p['size']) || 10,
        sort: null,
        search: null,
      });
    });
  }
}
