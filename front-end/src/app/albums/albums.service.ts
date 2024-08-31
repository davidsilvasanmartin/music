import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { PageEvent } from '@angular/material/paginator';

import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { ApiService } from '../api/api.service';
import { PageableResource } from '../api/api-pageable-resource-request';
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
    private readonly _apiService: ApiService
  ) {}

  getAlbums(pageEvent: PageEvent): Observable<PageableResource<Album[]>> {
    return this._http
      .get<PageableResource<AlbumDto[]>>(
        this._apiService.createApiUrl('albums'),
        {
          params: new HttpParams({
            fromObject: {
              page: pageEvent?.pageIndex,
              size: pageEvent?.pageSize,
            },
          }),
        }
      )
      .pipe(
        map((albumsResource: PageableResource<AlbumDto[]>) => ({
          ...albumsResource,
          content: albumsResource.content.map((a: AlbumDto) =>
            this._albumsMapper.fromDto(a)
          ),
        }))
      );
  }
}
