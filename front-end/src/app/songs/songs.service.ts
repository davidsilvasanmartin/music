import { Injectable } from '@angular/core';

import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import type { Album } from '../albums/album';
import type { AlbumDto } from '../albums/album-dto';
import { AlbumsMapperService } from '../albums/albums-mapper.service';
import { ApiService } from '../shared/api/api.service';
import type { Song } from './song';
import type { SongDto } from './song-dto';
import { SongsMapperService } from './songs-mapper.service';

@Injectable({ providedIn: 'root' })
export class SongsService {
  constructor(
    private readonly _apiService: ApiService,
    private readonly _songsMapperService: SongsMapperService,
    private readonly _albumsMapperService: AlbumsMapperService,
  ) {}

  getSong(songId: number): Observable<Song> {
    return this._apiService
      .get<SongDto>(`songs/${songId}`)
      .pipe(
        map((songDto: SongDto) => this._songsMapperService.fromDto(songDto)),
      );
  }

  getSongAlbum(songId: number): Observable<Album> {
    return this._apiService
      .get<AlbumDto>(`songs/${songId}/album`)
      .pipe(map((albumDto) => this._albumsMapperService.fromDto(albumDto)));
  }
}
