import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';

import { EMPTY, Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';

import { environment } from '../../../environments/environment';
import type { PaginationSortSearchParams } from '../../ui/pagination-sort-search';
import { SearchMapperService } from '../../ui/search';
import { SortMapperService } from '../../ui/sort';

@Injectable({ providedIn: 'root' })
export class ApiService {
  private readonly _url = environment.apiUrl;

  constructor(
    private readonly _http: HttpClient,
    private readonly _sortMapperService: SortMapperService,
    private readonly _searchMapperService: SearchMapperService,
  ) {}

  createApiUrl(resource: string): string {
    const resourceWithoutFrontSlash = resource.replace(/^\//, '');
    return `${this._url}/${resourceWithoutFrontSlash}`;
  }

  get<T>(
    url: string,
    params: Partial<PaginationSortSearchParams> | Record<string, unknown> = {},
  ): Observable<T> {
    return this._http
      .get<T>(this.createApiUrl(url), {
        params: this._createHttpParams(params),
      })
      .pipe(
        catchError((err) => {
          // TODO display error message in popup
          console.error(err);
          return EMPTY;
        }),
      );
  }

  getAndThrowError<T>(
    url: string,
    params: Partial<PaginationSortSearchParams> | Record<string, unknown> = {},
  ): Observable<T> {
    return this._http
      .get<T>(this.createApiUrl(url), {
        params: this._createHttpParams(params),
      })
      .pipe(
        catchError((err) => {
          // TODO display error message in popup
          console.error(err);
          return throwError(() => err);
        }),
      );
  }

  post<T>(url: string, body: unknown): Observable<T> {
    return this._http.post<T>(this.createApiUrl(url), body);
  }

  private _createHttpParams(
    params: Partial<PaginationSortSearchParams> & Record<string, unknown>,
  ): HttpParams {
    const { page, size, sort, search, ...restOfParams } = params;
    const queryParams: Record<string, string | number | boolean> = {};

    // We assume the PaginationSortSearchParams have the correct types
    if (page) {
      queryParams['page'] = page;
    }
    if (size) {
      queryParams['size'] = size;
    }
    if (sort) {
      queryParams['sort'] = this._sortMapperService.toQueryParam(sort);
    }
    if (search) {
      queryParams['search'] = this._searchMapperService.toQueryParam(search);
    }

    for (const key of Object.keys(restOfParams)) {
      const value = restOfParams[key];
      // Only include parameters with compatible types
      if (
        typeof value === 'string' ||
        typeof value === 'number' ||
        typeof value === 'boolean'
      ) {
        queryParams[key] = value;
      } else {
        console.warn(
          `ApiService.get: Parameter '${key}' has an unsupported type (${typeof value}) and will be ignored.`,
        );
      }
    }

    return new HttpParams({ fromObject: queryParams });
  }
}
