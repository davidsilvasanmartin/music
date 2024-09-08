export type SearchCondition = 'contains';

export interface Search {
  field: string;
  condition: SearchCondition;
  value: string;
}
