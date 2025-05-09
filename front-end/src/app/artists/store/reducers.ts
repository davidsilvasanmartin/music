import { ActionReducerMap } from '@ngrx/store';

import { artistsListReducer } from '../artists-list/store/reducers';
import { artistsViewReducer } from '../artists-view/store/reducers';
import type { ArtistsState } from './state';

export const artistsReducer: ActionReducerMap<ArtistsState> = {
  list: artistsListReducer,
  view: artistsViewReducer,
};
