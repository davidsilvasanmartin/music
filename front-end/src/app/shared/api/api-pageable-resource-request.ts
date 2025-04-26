export interface PageableResource<T> {
  content: T;
  number: number;
  size: number;
  totalElements: number;
}

export interface ApiPageableResourceRequest<T> {
  data: PageableResource<T> | null;
  loading: boolean;
  error: any;
}
