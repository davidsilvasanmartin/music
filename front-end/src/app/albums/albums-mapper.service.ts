import { Injectable } from '@angular/core';

import { ApiService } from '../api/api.service';
import type { Artist } from '../artists/artist';
import type { ArtistDto } from '../artists/artist-dto';
import type { Album } from './album';
import type { AlbumDto } from './album-dto';

@Injectable({ providedIn: 'root' })
export class AlbumsMapper {
  constructor(private readonly _apiService: ApiService) {}

  fromDto(albumDto: AlbumDto): Album {
    return {
      id: albumDto.id,
      artPathUrl: this._apiService.createApiUrl(
        `/albums/${albumDto.id}/albumArt`,
      ),
      artist: this.fromArtistDto(albumDto.artist),
      album: albumDto.album,
      genres: albumDto.genres,
      year: albumDto.year,
      // TODO
      songs: albumDto.songs,
    };
  }

  fromArtistDto(artistDto: ArtistDto): Artist {
    return {
      id: artistDto.id,
      name: artistDto.name,
    };
  }
}
