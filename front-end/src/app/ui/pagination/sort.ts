export enum SortDirection {
  DESC = 'desc',
  ASC = 'asc',
}

export interface Sort {
  field: string;
  direction: SortDirection;
}
