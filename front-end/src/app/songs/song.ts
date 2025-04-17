import type { Album } from '../albums/album';

export interface Song {
  id: number;
  title: string;
  lyrics?: string;
  album?: Album;
}
