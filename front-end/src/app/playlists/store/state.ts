import type { AppState } from '../../store/state';
import type { PlaylistsListState } from '../playlists-list/store/state';
import type { PlaylistsViewState } from '../playlists-view/store/state';

export interface PlaylistsRootState extends AppState {
  playlists: PlaylistsState;
}

export interface PlaylistsState {
  list: PlaylistsListState;
  view: PlaylistsViewState;
}
