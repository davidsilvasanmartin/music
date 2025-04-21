import { Pipe, PipeTransform } from '@angular/core';

import type { Song } from '../songs/song';
import type { Playlist } from './models';

@Pipe({ name: 'extractSongs' })
export class ExtractSongsPipe implements PipeTransform {
  transform(playlist: Playlist): Song[] {
    return playlist.items.map((i) => i.song);
  }
}
