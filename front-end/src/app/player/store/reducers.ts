import { Action, createReducer, on } from '@ngrx/store';

import * as playerActions from './actions';
import { playerInitialState, PlayerState } from './state';

const reducer = createReducer(
  playerInitialState,
  on(playerActions.addToPlaylist, (state, { songs }) => ({
    playlist: [...state.playlist, ...songs],
  })),
  on(playerActions.next, (state) => ({
    playlist: state.playlist.slice(1),
  }))
);

export const playerReducer = (state: PlayerState, action: Action) =>
  reducer(state, action);
