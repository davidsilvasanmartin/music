import { Injectable } from '@angular/core';

import { type Sort, SortDirection } from './sort';

@Injectable({ providedIn: 'root' })
export class SortMapperService {
  toObject(queryParam: string): Sort {
    const [field, direction] = queryParam.split(',');
    return {
      field,
      direction:
        direction.toLowerCase() == SortDirection.ASC.toLowerCase()
          ? SortDirection.ASC
          : SortDirection.DESC,
    };
  }

  toQueryParam(sort: Sort): string {
    return `${sort.field},${sort.direction.toLowerCase()}`;
  }
}
