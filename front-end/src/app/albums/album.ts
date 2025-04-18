import type { Artist } from '../artists/artist';
import type { Genre } from '../genres/genre';
import type { Song } from '../songs/song';

export interface Album {
  id: number;
  artPathUrl: string;
  artist?: Artist;
  album: string;
  genres: Genre[];
  year: number;
  songs: Song[];
}
