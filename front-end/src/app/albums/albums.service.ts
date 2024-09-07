import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';

import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { ApiService } from '../api/api.service';
import { PageableResource } from '../api/api-pageable-resource-request';
import { PaginationSortFilterParams } from '../ui/pagination-sort-filter/pagination-sort-filter-params';
import { SortMapperService } from '../ui/sort/sort-mapper.service';
import { Album } from './album';
import { AlbumDto } from './album-dto';
import { AlbumsMapper } from './albums-mapper.service';

@Injectable({ providedIn: 'root' })
export class AlbumsService {
  constructor(
    private readonly _http: HttpClient,
    private readonly _albumsMapper: AlbumsMapper,
    private readonly _apiService: ApiService,
    private readonly _sortMapperService: SortMapperService,
  ) {}

  getAlbums(
    paginationParams: PaginationSortFilterParams,
  ): Observable<PageableResource<Album[]>> {
    return this._http
      .get<PageableResource<AlbumDto[]>>(
        this._apiService.createApiUrl('albums'),
        {
          params: new HttpParams({
            fromObject: {
              page: paginationParams.page,
              size: paginationParams.size,
              sort: this._sortMapperService.toQueryParam(paginationParams.sort),
            },
          }),
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
