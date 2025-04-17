import type { ApiPageableResourceRequest } from '../../api/api-pageable-resource-request';
import type { AppState } from '../../store/state';
import type { Artist } from '../artist';

export interface ArtistsRootState extends AppState {
  artists: ArtistsState;
}

export interface ArtistsState {
  artists: ApiPageableResourceRequest<Artist[]>;
}

export const artistsInitialState: ArtistsState = {
  artists: { data: null, loading: false, error: null },
};
