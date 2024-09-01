import type { Sort } from './sort';

export interface PaginationParams {
  page: number;
  size: number;
  sort: Sort;
  // TODO object
  search: string;
}
