import type { PlaylistItem } from './playlist-item';

export interface Playlist {
  id: number;
  name: string;
  description: string;
  items: PlaylistItem[];
}
