import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';

import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { ApiService } from '../api/api.service';
import type { PageableResource } from '../api/api-pageable-resource-request';
import type { PaginationSortSearchParams } from '../ui/pagination-sort-search';
import { SearchMapperService } from '../ui/search';
import { SortMapperService } from '../ui/sort';
import type { Album } from './album';
import type { AlbumDto } from './album-dto';
import { AlbumsMapper } from './albums-mapper.service';

@Injectable({ providedIn: 'root' })
export class AlbumsService {
  constructor(
    private readonly _http: HttpClient,
    private readonly _albumsMapper: AlbumsMapper,
    private readonly _apiService: ApiService,
    private readonly _sortMapperService: SortMapperService,
    private readonly _searchMapperService: SearchMapperService,
  ) {}

  getAlbums(
    params: PaginationSortSearchParams,
  ): Observable<PageableResource<Album[]>> {
    const paramsObject: Record<string, string | number> = {
      page: params.page,
      size: params.size,
      sort: this._sortMapperService.toQueryParam(params.sort),
    };
    if (params.search) {
      paramsObject['search'] = this._searchMapperService.toQueryParam(
        params.search,
      );
    }

    return this._http
      .get<PageableResource<AlbumDto[]>>(
        this._apiService.createApiUrl('albums'),
        {
          params: new HttpParams({ fromObject: paramsObject }),
        },
      )
      .pipe(
        map((albumsResource: PageableResource<AlbumDto[]>) => ({
          ...albumsResource,
          content: albumsResource.content.map((a: AlbumDto) =>
            this._albumsMapper.fromDto(a),
          ),
        })),
      );
  }
}
