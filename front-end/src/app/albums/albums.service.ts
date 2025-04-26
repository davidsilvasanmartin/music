import { Injectable } from '@angular/core';

import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { ApiService } from '../shared/api/api.service';
import type { PageableResource } from '../shared/api/api-pageable-resource-request';
import type { PaginationSortSearchParams } from '../ui/pagination-sort-search';
import type { Album } from './album';
import type { AlbumDto } from './album-dto';
import { AlbumsMapperService } from './albums-mapper.service';

@Injectable({ providedIn: 'root' })
export class AlbumsService {
  constructor(
    private readonly _albumsMapper: AlbumsMapperService,
    private readonly _apiService: ApiService,
  ) {}

  getAlbums(
    params: PaginationSortSearchParams,
  ): Observable<PageableResource<Album[]>> {
    return this._apiService
      .get<PageableResource<AlbumDto[]>>('albums', params)
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
