import type { PlaylistItemDto } from './playlist-item-dto';

export interface PlaylistDto {
  id?: number;
  name: string;
  description: string;
  items: PlaylistItemDto[];
}
