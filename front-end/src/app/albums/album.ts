import type { Artist } from '../artists/artist';
import type { Song } from '../songs/song';

export interface Album {
  id: number;
  artPathUrl: string;
  artist: Artist;
  album: string;
  genres: string[];
  year: number;
  songs: Song[];
}
