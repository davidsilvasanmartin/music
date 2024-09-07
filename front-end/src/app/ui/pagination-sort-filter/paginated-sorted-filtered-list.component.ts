import { Directive, inject } from "@angular/core";
import { ActivatedRoute, Params } from "@angular/router";

import { BehaviorSubject, filter } from "rxjs";

import { Sort } from "../sort/sort";
import { SortMapperService } from "../sort/sort-mapper.service";
import { PaginationSortFilterParams } from "./pagination-sort-filter-params";

/**
 * @TODO !! I don't like this approach because the dto objects bleed into the
 * components
 */
@Directive({})
export abstract class PaginatedSortedFilteredListComponent<DtoClass> {
  protected sortableFilterableFields: (keyof DtoClass)[];
  private readonly _params$ =
    new BehaviorSubject<PaginationSortFilterParams | null>(null);
  protected readonly params$ = this._params$
    .asObservable()
    .pipe(filter(Boolean));

  protected constructor(
    sortableFilterableFields: (keyof DtoClass)[],
    private readonly _defaultSort: Sort,
  ) {
    this.sortableFilterableFields = sortableFilterableFields;
    const activatedRoute = inject(ActivatedRoute);
    const sortMapperService = inject(SortMapperService);
    activatedRoute.queryParams.subscribe((p: Params) => {
      this._params$.next({
        page: Number(p['page']) || 1,
        size: Number(p['size']) || 10,
        sort: p['sort']
          ? sortMapperService.toObject(p['sort'])
          : this._defaultSort,
      });
    });
  }
}
