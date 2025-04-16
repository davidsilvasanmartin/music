import type { Album } from '../albums/album';

export interface Artist {
  id: number;
  name: string;
  mbArtistId: string;
  albums?: Album[];
}
