import { Song } from '../songs/song';

export interface Album {
  id: number;
  artPath: string;
  albumArtist: string;
  album: string;
  genre: string;
  year: number;
  songs: Song[];
}
