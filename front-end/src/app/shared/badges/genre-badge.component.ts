import { ChangeDetectionStrategy, Component, input } from '@angular/core';
import { Params } from '@angular/router';

import type { Genre } from '../../genres/genre';
import { SearchMapperService } from '../../ui/search';

@Component({
  selector: 'app-genre-badge',
  template: `<a
    [routerLink]="['.']"
    [queryParams]="getSearchQueryParamsForGenre(genre())"
    queryParamsHandling="merge"
    class="rounded-md border-[1px] border-amber-700 bg-amber-50 px-2 py-0.5 text-sm text-amber-700 hover:border-amber-500 hover:text-amber-500 hover:underline"
    >{{ genre().name }}</a
  >`,
  styles: [':host { display: flex; }'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class GenreBadgeComponent {
  genre = input.required<Genre>();

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
