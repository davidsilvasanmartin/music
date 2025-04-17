import type { ApiPageableResourceRequest } from '../../api/api-pageable-resource-request';
import type { AppState } from '../../store/state';
import type { Album } from '../album';

export interface AlbumsRootState extends AppState {
  albums: AlbumsState;
}

export interface AlbumsState {
  albums: ApiPageableResourceRequest<Album[]>;
}

export const albumsInitialState: AlbumsState = {
  albums: { data: null, loading: false, error: null },
};
