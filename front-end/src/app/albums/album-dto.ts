import type { ArtistDto } from '../artists/artist-dto';
import type { Song } from '../songs/song';

export interface AlbumDto {
  id: number;
  artPath: string;
  artist: ArtistDto;
  album: string;
  genres: string[];
  year: number;
  // TODO
  songs: Song[];
}
