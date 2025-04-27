import { ChangeDetectionStrategy, Component, input } from '@angular/core';
import { Params } from '@angular/router';

import type { Genre } from '../../genres/genre';
import { SearchMapperService } from '../../ui/search';

/**
 * TODO genre query params are specific to albums component
 *  - We can add an output so that the parent component performs the filtering
 *  - ... or an input for the field name
 */
@Component({
  selector: 'app-genre-badge',
  template: `
    <span
      class="inline-flex flex-row flex-nowrap items-center gap-1 overflow-hidden rounded-md border-[1px] border-amber-700 bg-amber-50 px-1 py-0.5"
    >
      <a
        [routerLink]="['/genres', genre().id]"
        class="text-genre hover:underline"
        >{{ genre().name }}</a
      >
      @if (showFilter()) {
        <span class="h-4 border-r-[1px] border-amber-400"></span>
        <a
          class="text-genre"
          [routerLink]="['.']"
          [queryParams]="getSearchQueryParamsForGenre(genre())"
          queryParamsHandling="merge"
          title="Filter by genre: {{ genre().name }}"
        >
          <app-icon-filter class="size-3.5" />
        </a>
      }
    </span>
  `,
  styles: [':host { display: contents; }'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class GenreBadgeComponent {
  genre = input.required<Genre>();
  showFilter = input<boolean>(false);

  constructor(private readonly _searchMapperService: SearchMapperService) {}

  getSearchQueryParamsForGenre(genre: Genre): Params {
    return {
      search: this._searchMapperService.toQueryParam({
        field: 'genres.name',
        condition: 'contains',
        value: genre.name,
      }),
      page: 1,
    };
  }
}
