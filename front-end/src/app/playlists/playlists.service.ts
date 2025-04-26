import { Injectable } from '@angular/core';

import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { ApiService } from '../shared/api/api.service';
import type { Playlist, PlaylistDto } from './models';
import { PlaylistsMapper } from './playlists-mapper.service';

@Injectable({ providedIn: 'root' })
export class PlaylistsService {
  constructor(
    private readonly _playlistsMapper: PlaylistsMapper,
    private readonly _apiService: ApiService,
  ) {}

  getPlaylists(): Observable<Playlist[]> {
    return this._apiService
      .get<PlaylistDto[]>('playlists')
      .pipe(
        map((playlistDtos: PlaylistDto[]) =>
          playlistDtos.map((p) => this._playlistsMapper.fromDto(p)),
        ),
      );
  }

  getPlaylist(id: number) {
    return this._apiService
      .get<PlaylistDto>(`playlists/${id}`)
      .pipe(map((p: PlaylistDto) => this._playlistsMapper.fromDto(p)));
  }

  createPlaylist(playlist: Playlist): Observable<Playlist> {
    return this._apiService
      .post<PlaylistDto>('playlists', playlist)
      .pipe(map((p: PlaylistDto) => this._playlistsMapper.fromDto(p)));
  }
}
