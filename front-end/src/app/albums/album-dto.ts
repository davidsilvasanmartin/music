import { Song } from '../songs/song';

export interface AlbumDto {
  id: number;
  artPath: string;
  albumArtist: string;
  album: string;
  genres: string[];
  year: number;
  // TODO
  songs: Song[];
}
