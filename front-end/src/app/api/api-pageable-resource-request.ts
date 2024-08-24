export interface PageableResource<T> {
  content: T;
  number: number;
  size: number;
  totalElements: number;
}

export interface ApiPageableResourceRequest<T> {
  data: PageableResource<T>;
  loading: boolean;
  error: any;
}
