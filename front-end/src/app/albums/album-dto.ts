import { Song } from '../songs/song';

export interface AlbumDto {
  id: number;
  artPath: string;
  albumArtist: string;
  album: string;
  genre: string;
  year: number;
  // TODO
  songs: Song[];
}
