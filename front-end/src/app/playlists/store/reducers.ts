import { ActionReducerMap } from '@ngrx/store';

import { playlistsListReducer } from '../playlists-list/store/reducers';
import { playlistsViewReducer } from '../playlists-view/store/reducers';
import type { PlaylistsState } from './state';

export const playlistsReducer: ActionReducerMap<PlaylistsState> = {
  list: playlistsListReducer,
  view: playlistsViewReducer,
};
