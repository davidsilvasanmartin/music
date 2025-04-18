import type { AppState } from '../../store/state';
import type { ArtistsListState } from '../artists-list/store/state';

export interface ArtistsRootState extends AppState {
  artists: ArtistsState;
}

export interface ArtistsState {
  list: ArtistsListState;
}
