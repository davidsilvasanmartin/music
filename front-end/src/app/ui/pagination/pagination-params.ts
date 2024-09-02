import type { Sort } from './sort';

export interface PaginationParams {
  page: number;
  size: number;
  sort: Sort | null;
  // TODO object
  search: string | null;
}
