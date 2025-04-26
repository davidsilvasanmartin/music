import type { ApiRequest } from '../../../shared/api/api-request';
import type { Playlist } from '../../models';

export interface PlaylistsListState {
  playlists: ApiRequest<Playlist[]>;
}

export const playlistsListInitialState: PlaylistsListState = {
  playlists: { data: [], loading: false, error: null },
};
