import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';

import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { ApiService } from '../api/api.service';
import type { PageableResource } from '../api/api-pageable-resource-request';
import type { PaginationSortSearchParams } from '../ui/pagination-sort-search';
import { SearchMapperService } from '../ui/search';
import { SortMapperService } from '../ui/sort';
import type { Artist } from './artist';
import type { ArtistDto } from './artist-dto';
import { ArtistsMapper } from './artists-mapper.service';

@Injectable({ providedIn: 'root' })
export class ArtistsService {
  constructor(
    private readonly _http: HttpClient,
    private readonly _artistsMapper: ArtistsMapper,
    private readonly _apiService: ApiService,
    private readonly _sortMapperService: SortMapperService,
    private readonly _searchMapperService: SearchMapperService,
  ) {}

  getArtists(
    params: PaginationSortSearchParams,
  ): Observable<PageableResource<Artist[]>> {
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
      .get<PageableResource<ArtistDto[]>>(
        this._apiService.createApiUrl('artists'),
        {
          params: new HttpParams({ fromObject: paramsObject }),
        },
      )
      .pipe(
        map((artistsResource: PageableResource<ArtistDto[]>) => ({
          ...artistsResource,
          content: artistsResource.content.map((a: ArtistDto) =>
            this._artistsMapper.fromDto(a),
          ),
        })),
      );
  }
}
