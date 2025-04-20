import type { SongDto } from '../../songs/song-dto';

export interface PlaylistItemDto {
  id: number;
  position: number;
  song: SongDto;
  // I think for now I'm not going to use
  // the cached properties in the front-end,
  // since we should always have a song
  mbTrackId?: string;
  songTitle?: string;
  mbAlbumId?: string;
  albumTitle?: string;
}
