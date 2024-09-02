import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';

import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { ApiService } from '../api/api.service';
import { PageableResource } from '../api/api-pageable-resource-request';
import { PaginationParams } from '../ui/pagination/pagination-params';
import { Album } from './album';
import { AlbumDto } from './album-dto';
import { AlbumsMapper } from './albums-mapper.service';

@Injectable({
  providedIn: 'root',
})
export class AlbumsService {
  constructor(
    private readonly _http: HttpClient,
    private readonly _albumsMapper: AlbumsMapper,
    private readonly _apiService: ApiService,
  ) {}

  getAlbums(
    paginationParams: PaginationParams,
  ): Observable<PageableResource<Album[]>> {
    return this._http
      .get<PageableResource<AlbumDto[]>>(
        this._apiService.createApiUrl('albums'),
        {
          params: new HttpParams({
            fromObject: {
              page: paginationParams.page,
              size: paginationParams.size,
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
