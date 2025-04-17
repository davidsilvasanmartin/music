import type { AlbumDto } from '../albums/album-dto';

export interface SongDto {
  id: number;
  title: string;
  lyrics?: string;
  album?: AlbumDto;
}
