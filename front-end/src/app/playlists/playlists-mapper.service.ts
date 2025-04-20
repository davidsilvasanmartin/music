import { Injectable } from '@angular/core';

import type { Song } from '../songs/song';
import type { SongDto } from '../songs/song-dto';
import type {
  Playlist,
  PlaylistDto,
  PlaylistItem,
  PlaylistItemDto,
} from './models';

@Injectable({ providedIn: 'root' })
export class PlaylistsMapper {
  fromDto(playlistDto: PlaylistDto): Playlist {
    return {
      id: playlistDto.id,
      name: playlistDto.name,
      description: playlistDto.description,
      items: playlistDto.items?.map((i) => this.fromPlaylistItemDto(i)) || [],
    };
  }

  fromPlaylistItemDto(playlistItemDto: PlaylistItemDto): PlaylistItem {
    return {
      id: playlistItemDto.id,
      position: playlistItemDto.position,
      song: this.fromSongDto(playlistItemDto.song),
    };
  }

  fromSongDto(songDto: SongDto): Song {
    return {
      id: songDto.id,
      title: songDto.title,
    };
  }
}
