import type { AlbumDto } from '../albums/album-dto';

export interface ArtistDto {
  id: number;
  name: string;
  mbArtistId: string;
  albums?: AlbumDto[];
  createdAt?: string;
  updatedAt?: number;
}
