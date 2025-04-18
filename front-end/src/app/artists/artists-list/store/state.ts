import type { ApiPageableResourceRequest } from '../../../api/api-pageable-resource-request';
import type { Artist } from '../../artist';

export interface ArtistsListState {
  artists: ApiPageableResourceRequest<Artist[]>;
}

export const artistsInitialState: ArtistsListState = {
  artists: { data: null, loading: false, error: null },
};
