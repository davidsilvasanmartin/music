import { PaginationParams } from '../pagination/pagination-params';
import { Sort } from '../sort/sort';

export type PaginationSortFilterParams = PaginationParams & { sort: Sort };
