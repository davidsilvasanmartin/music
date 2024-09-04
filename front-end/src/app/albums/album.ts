import { Song } from '../songs/song';

export interface Album {
  id: number;
  artPathUrl: string;
  albumArtist: string;
  album: string;
  genres: string[];
  year: number;
  songs: Song[];
}
