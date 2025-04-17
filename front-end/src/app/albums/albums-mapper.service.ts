import { Injectable } from '@angular/core';

import { ApiService } from '../api/api.service';
import type { Artist } from '../artists/artist';
import type { ArtistDto } from '../artists/artist-dto';
import type { Song } from '../songs/song';
import type { SongDto } from '../songs/song-dto';
import type { Album } from './album';
import type { AlbumDto } from './album-dto';

@Injectable({ providedIn: 'root' })
export class AlbumsMapperService {
  constructor(private readonly _apiService: ApiService) {}

  fromDto(albumDto: AlbumDto): Album {
    return {
      id: albumDto.id,
      artPathUrl: this._apiService.createApiUrl(
        `/albums/${albumDto.id}/albumArt`,
      ),
      artist: this._fromArtistDto(albumDto.artist),
      album: albumDto.album,
      genres: albumDto.genres,
      year: albumDto.year,
      songs: albumDto.songs?.map((songDto) => this._fromSongDto(songDto)) || [],
    };
  }

  private _fromArtistDto(artistDto: ArtistDto | undefined): Artist | undefined {
    if (!artistDto) {
      return undefined;
    }
    return {
      id: artistDto.id,
      name: artistDto.name,
    };
  }

  private _fromSongDto(songDto: SongDto): Song {
    return {
      id: songDto.id,
      title: songDto.title,
    };
  }
}
