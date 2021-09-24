import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { ApiService } from '../api/api.service';
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

  getAlbums(): Observable<Album[]> {
    return this._http
      .get<AlbumDto[]>(this._apiService.createApiUrl('albums'))
      .pipe(
        map((albums: AlbumDto[]) =>
          albums.map((a: AlbumDto) => this._albumsMapper.fromDto(a))
        )
      );
  }
}
