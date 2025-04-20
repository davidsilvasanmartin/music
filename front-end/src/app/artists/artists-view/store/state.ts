import type { ApiRequest } from '../../../api/api-request';
import type { Artist } from '../../artist';

export interface ArtistsViewState {
  artist: ApiRequest<Artist | null>;
}

export const artistsViewInitialState: ArtistsViewState = {
  artist: { data: null, loading: false, error: null },
};
