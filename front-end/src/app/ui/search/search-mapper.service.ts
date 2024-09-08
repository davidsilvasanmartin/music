import { Injectable } from '@angular/core';

import type { Search, SearchCondition } from './search';

@Injectable({ providedIn: 'root' })
export class SearchMapperService {
  toObject(queryParam: string): Search {
    const [field, condition, value] = queryParam.split(':');
    return {
      field,
      condition: condition as SearchCondition,
      value,
    };
  }

  toQueryParam(search: Search): string {
    return `${search.field}:${search.condition}:${search.value}`;
  }
}
