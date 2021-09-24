import { ApiRequest } from '../../api/api-request';
import { AppState } from '../../store/state';
import { Album } from '../album';

export interface AlbumsRootState extends AppState {
  albums: AlbumsState;
}

export interface AlbumsState {
  albums: ApiRequest<Album[]>;
}

export const albumsInitialState: AlbumsState = {
  albums: { data: [], loading: false, error: null },
};
