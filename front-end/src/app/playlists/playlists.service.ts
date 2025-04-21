import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { ApiService } from '../api/api.service';
import type { Playlist, PlaylistDto } from './models';
import { PlaylistsMapper } from './playlists-mapper.service';

@Injectable({ providedIn: 'root' })
export class PlaylistsService {
  constructor(
    private readonly _http: HttpClient,
    private readonly _playlistsMapper: PlaylistsMapper,
    private readonly _apiService: ApiService,
  ) {}

  getPlaylists(): Observable<Playlist[]> {
    return this._http
      .get<PlaylistDto[]>(this._apiService.createApiUrl('playlists'))
      .pipe(
        map((playlistDtos: PlaylistDto[]) =>
          playlistDtos.map((p) => this._playlistsMapper.fromDto(p)),
        ),
      );
  }

  getPlaylist(id: number) {
    return this._http
      .get<PlaylistDto>(this._apiService.createApiUrl(`playlists/${id}`))
      .pipe(map((p: PlaylistDto) => this._playlistsMapper.fromDto(p)));
  }

  createPlaylist(playlist: Playlist): Observable<Playlist> {
    return this._http
      .post<PlaylistDto>(this._apiService.createApiUrl('playlists'), playlist)
      .pipe(map((p: PlaylistDto) => this._playlistsMapper.fromDto(p)));
  }
}
