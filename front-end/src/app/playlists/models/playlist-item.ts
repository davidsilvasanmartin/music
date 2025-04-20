import type { Song } from '../../songs/song';

export interface PlaylistItem {
  id: number;
  position: number;
  song: Song;
  // I think for now I'm not going to use
  // the cached properties in the front-end,
  // since we should always have a song
  mbTrackId?: string;
  songTitle?: string;
  mbAlbumId?: string;
  albumTitle?: string;
}
