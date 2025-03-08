import { AppState } from '../../store/state';
import { User } from '../models';

export interface AuthRootState extends AppState {
  auth: AuthState;
}

export interface AuthState {
  user: User | null;
}

export const authInitialState: AuthState = {
  user: null,
};
