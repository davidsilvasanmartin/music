import { Action, createReducer, on } from '@ngrx/store';

import { addToQueueSuccess, next, removeFromQueue, reset } from './actions';
import { queueInitialState, type QueueState } from './state';

const reducer = createReducer(
  queueInitialState,
  on(addToQueueSuccess, (state, { songs }) => ({
    ...state,
    queue: [...state.queue, ...songs],
  })),
  on(removeFromQueue, (state, { songIndex }) => ({
    ...state,
    queue: state.queue
      .slice(0, songIndex)
      .concat(state.queue.slice(songIndex + 1)),
  })),
  on(next, (state) => ({
    ...state,
    queue: state.queue.slice(1),
  })),
  on(reset, () => queueInitialState),
);

export const queueReducer = (state: QueueState, action: Action) =>
  reducer(state, action);
