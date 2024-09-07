import { Component, input } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { Sort, SortDirection } from './sort';
import { SortMapperService } from './sort-mapper.service';

@Component({
  selector: 'app-sort',
  template: `
    <div
      class="flex flex-row flex-wrap items-center gap-1.5 italic text-gray-700"
    >
      <span>Sort by</span>
      <select
        (ngModelChange)="onFieldChanged($event)"
        [ngModel]="sort().field"
        class="rounded-sm bg-white text-center italic"
      >
        @for (field of sortableFields(); track field) {
          <option [value]="field">{{ field }}</option>
        }
      </select>
      <select
        (ngModelChange)="onDirectionChanged($event)"
        [ngModel]="sort().direction"
        class="rounded-sm bg-white text-center italic"
      >
        <option [value]="sortDirection.ASC">ASC</option>
        <option [value]="sortDirection.DESC">DESC</option>
      </select>
    </div>
  `,
})
export class SortComponent {
  sort = input.required<Sort>();
  sortableFields = input.required<string[]>();
  sortDirection = SortDirection;

  constructor(
    private readonly _router: Router,
    private readonly _activatedRoute: ActivatedRoute,
    private readonly _sortMapperService: SortMapperService,
  ) {}

  onFieldChanged(field: string) {
    this._router.navigate([], {
      relativeTo: this._activatedRoute,
      queryParams: {
        sort: this._sortMapperService.toQueryParam({
          ...this.sort(),
          field,
        }),
      },
      queryParamsHandling: 'merge',
    });
  }

  onDirectionChanged(direction: SortDirection) {
    this._router.navigate([], {
      relativeTo: this._activatedRoute,
      queryParams: {
        sort: this._sortMapperService.toQueryParam({
          ...this.sort(),
          direction,
        }),
      },
      queryParamsHandling: 'merge',
    });
  }
}
