import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import type { Album } from '../albums/album';
import type { AlbumDto } from '../albums/album-dto';
import { AlbumsMapperService } from '../albums/albums-mapper.service';
import { ApiService } from '../shared/api/api.service';
import type { Song } from './song';
import type { SongDto } from './song-dto';

@Injectable({ providedIn: 'root' })
export class SongsService {
  constructor(
    private readonly _http: HttpClient,
    private readonly _apiService: ApiService,
    private readonly _albumsMapperService: AlbumsMapperService,
  ) {}

  // TODO map the SongDto to a Song
  getSong(songId: number): Observable<Song> {
    return this._http.get<SongDto>(
      this._apiService.createApiUrl(`songs/${songId}`),
    ) as Observable<Song>;
  }

  getSongAlbum(songId: number): Observable<Album> {
    return this._http
      .get<AlbumDto>(this._apiService.createApiUrl(`songs/${songId}/album`))
      .pipe(map((albumDto) => this._albumsMapperService.fromDto(albumDto)));
  }
}
