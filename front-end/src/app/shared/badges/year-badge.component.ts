import { ChangeDetectionStrategy, Component, input } from '@angular/core';
import { Params } from '@angular/router';

import { SearchMapperService } from '../../ui/search';

/**
 * TODO year query params are specific to albums component
 */
@Component({
  selector: 'app-year-badge',
  template: `
    <span
      class="inline-flex flex-row flex-nowrap items-center gap-1 overflow-hidden rounded-md border-[1px] border-slate-700 bg-slate-50 px-1 py-0.5 text-sm text-slate-700"
    >
      <span>{{ year() }}</span>
      <span class="h-4 border-r-[1px] border-slate-400"></span>
      <a
        [routerLink]="['.']"
        [queryParams]="getSearchQueryParamsForYear(year())"
        queryParamsHandling="merge"
        class="hover:text-slate-500"
        title="Filter by year: {{ year() }}"
      >
        <app-icon-filter class="size-3.5" />
      </a>
    </span>
  `,
  styles: [':host { display: contents; }'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class YearBadgeComponent {
  year = input.required<number>();

  constructor(private readonly _searchMapperService: SearchMapperService) {}

  getSearchQueryParamsForYear(year: number): Params {
    return {
      search: this._searchMapperService.toQueryParam({
        field: 'year',
        condition: 'contains',
        value: year.toString(),
      }),
      page: 1,
    };
  }
}
