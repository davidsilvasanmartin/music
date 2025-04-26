import { Injectable } from '@angular/core';

import { AlbumsMapperService } from '../albums/albums-mapper.service';
import type { Song } from './song';
import type { SongDto } from './song-dto';

@Injectable({ providedIn: 'root' })
export class SongsMapperService {
  constructor(private readonly _albumsMapper: AlbumsMapperService) {}

  fromDto(songDto: SongDto): Song {
    return {
      id: songDto.id,
      title: songDto.title,
      lyrics: songDto.lyrics,
      album: songDto.album
        ? this._albumsMapper.fromDto(songDto.album)
        : undefined,
    };
  }
}
