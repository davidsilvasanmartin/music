import type { AppState } from '../../store/state';
import type { ArtistsListState } from '../artists-list/store/state';
import type { ArtistsViewState } from '../artists-view/store/state';

export interface ArtistsRootState extends AppState {
  artists: ArtistsState;
}

export interface ArtistsState {
  list: ArtistsListState;
  view: ArtistsViewState;
}
