import { Injectable } from '@angular/core';

import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { ApiService } from '../shared/api/api.service';
import type { PageableResource } from '../shared/api/api-pageable-resource-request';
import type { PaginationSortSearchParams } from '../ui/pagination-sort-search';
import type { Artist } from './artist';
import type { ArtistDto } from './artist-dto';
import { ArtistsMapper } from './artists-mapper.service';

@Injectable({ providedIn: 'root' })
export class ArtistsService {
  constructor(
    private readonly _artistsMapper: ArtistsMapper,
    private readonly _apiService: ApiService,
  ) {}

  getArtists(
    params: PaginationSortSearchParams,
  ): Observable<PageableResource<Artist[]>> {
    return this._apiService
      .get<PageableResource<ArtistDto[]>>('artists', params)
      .pipe(
        map((artistsResource: PageableResource<ArtistDto[]>) => ({
          ...artistsResource,
          content: artistsResource.content.map((a: ArtistDto) =>
            this._artistsMapper.fromDto(a),
          ),
        })),
      );
  }

  getArtist(id: number) {
    return this._apiService
      .get<ArtistDto>(`artists/${id}`)
      .pipe(map((a: ArtistDto) => this._artistsMapper.fromDto(a)));
  }
}
