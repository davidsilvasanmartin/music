import { createFeatureSelector, createSelector } from '@ngrx/store';

import type { Song } from '../../songs/song';
import type { QueueState } from './state';

export const getQueueState = createFeatureSelector<QueueState>('queue');

export const getQueue = createSelector(
  getQueueState,
  (state: QueueState) => state.queue,
);

export const getCurrentSong = createSelector(
  getQueue,
  (queue: Song[]) => queue[0],
);

export const getNextSongs = createSelector(getQueue, (queue: Song[]) =>
  queue.length > 0 ? queue.slice(1) : [],
);
