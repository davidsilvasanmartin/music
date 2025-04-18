import { Injectable } from '@angular/core';

import { AlbumsMapperService } from '../albums/albums-mapper.service';
import type { Artist } from './artist';
import type { ArtistDto } from './artist-dto';

@Injectable({ providedIn: 'root' })
export class ArtistsMapper {
  constructor(private readonly _albumsMapper: AlbumsMapperService) {}

  fromDto(artistDto: ArtistDto): Artist {
    return {
      id: artistDto.id,
      name: artistDto.name,
      mbArtistId: artistDto.mbArtistId,
      albums:
        artistDto.albums?.map((albumDto) =>
          this._albumsMapper.fromDto(albumDto),
        ) || [],
    };
  }
}
