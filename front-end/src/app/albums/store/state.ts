import { ApiPageableResourceRequest } from 'src/app/api/api-pageable-resource-request';
import { AppState } from '../../store/state';
import { Album } from '../album';

export interface AlbumsRootState extends AppState {
  albums: AlbumsState;
}

export interface AlbumsState {
  albums: ApiPageableResourceRequest<Album[]>;
}

export const albumsInitialState: AlbumsState = {
  albums: { data: null, loading: false, error: null },
};
