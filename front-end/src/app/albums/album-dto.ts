import type { ArtistDto } from '../artists/artist-dto';
import type { GenreDto } from '../genres/genre-dto';
import type { SongDto } from '../songs/song-dto';

export interface AlbumDto {
  id: number;
  artPath: string;
  artist?: ArtistDto;
  album: string;
  genres: GenreDto[];
  year: number;
  songs: SongDto[];
}
