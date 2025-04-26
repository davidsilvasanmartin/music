import type { Song } from '../../songs/song';
import type { AppState } from '../../store/state';

export interface QueueRootState extends AppState {
  queue: QueueState;
}

export interface QueueState {
  queue: Song[];
  currentSong: Song | null;
}

export const queueInitialState: QueueState = {
  queue: [],
  currentSong: null,
};
