import { Injectable } from '@angular/core';

import { ApiService } from '../api/api.service';
import { Album } from './album';
import { AlbumDto } from './album-dto';

@Injectable({ providedIn: 'root' })
export class AlbumsMapper {
  constructor(private readonly _apiService: ApiService) {}

  fromDto(albumDto: AlbumDto): Album {
    return {
      id: albumDto.id,
      artPathUrl: this._apiService.createApiUrl(
        `/albums/${albumDto.id}/albumArt`,
      ),
      albumArtist: albumDto.albumArtist,
      album: albumDto.album,
      genres: albumDto.genres,
      year: albumDto.year,
      // TODO
      songs: albumDto.songs,
    };
  }
}
