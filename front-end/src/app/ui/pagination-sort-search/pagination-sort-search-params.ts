import type { Pagination } from '../pagination';
import { Search } from '../search';
import type { Sort } from '../sort';

export type PaginationSortSearchParams = Pagination & {
  sort: Sort;
  search: Search | null;
};
