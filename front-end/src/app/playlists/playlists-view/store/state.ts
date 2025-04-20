import type { ApiRequest } from '../../../api/api-request';
import type { Playlist } from '../../models';

export interface PlaylistsViewState {
  playlist: ApiRequest<Playlist | null>;
}

export const playlistsViewInitialState: PlaylistsViewState = {
  playlist: { data: null, loading: false, error: null },
};
