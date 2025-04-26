import { createAction, props } from '@ngrx/store';

import type { Song } from '../../songs/song';

export enum QueueActionTypes {
  addToQueue = '[Queue] Add to queue',
  addToQueueSuccess = '[Queue] Add to queue Success',
  removeFromQueue = '[Queue] Remove from queue',
  next = '[Queue] Next',
  reset = '[Queue] Reset',
}

export const addToQueue = createAction(
  QueueActionTypes.addToQueue,
  props<{ songIds: number[] }>(),
);

export const addToQueueSuccess = createAction(
  QueueActionTypes.addToQueueSuccess,
  props<{ songs: Song[] }>(),
);

export const removeFromQueue = createAction(
  QueueActionTypes.removeFromQueue,
  props<{ songIndex: number }>(),
);

export const next = createAction(QueueActionTypes.next);

export const reset = createAction(QueueActionTypes.reset);
