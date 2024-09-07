import { Directive } from '@angular/core';
import { ActivatedRoute, Params } from '@angular/router';

import { BehaviorSubject, filter } from 'rxjs';

import { PaginationParams } from '../pagination-params';

@Directive({})
export abstract class PaginatedListComponent {
  private readonly _paginationParams$ =
    new BehaviorSubject<PaginationParams | null>(null);
  protected readonly paginationParams$ = this._paginationParams$
    .asObservable()
    .pipe(filter(Boolean));

  protected constructor(private readonly _activatedRoute: ActivatedRoute) {
    this._activatedRoute.queryParams.subscribe((p: Params) => {
      this._paginationParams$.next({
        page: Number(p['page']) || 1,
        size: Number(p['size']) || 10,
      });
    });
  }
}
