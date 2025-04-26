import { Component, input } from '@angular/core';

import { Store } from '@ngrx/store';

import * as queueActions from '../../queue/store/actions';
import type { QueueRootState } from '../../queue/store/state';

@Component({
  selector: 'app-queue-remove',
  template: `
    <button
      class="btn rounded-full bg-blue-500 p-1 font-bold text-white hover:bg-blue-700"
      aria-label="Remove from play queue"
      (click)="removeSongFromQueue()"
    >
      <app-icon-cancel class="size-4" />
    </button>
  `,
})
export class QueueRemoveComponent {
  /** The index of the song in the queue */
  songIndex = input.required<number>();

  constructor(private readonly _store: Store<QueueRootState>) {}

  removeSongFromQueue() {
    this._store.dispatch(
      queueActions.removeFromQueue({ songIndex: this.songIndex() }),
    );
  }
}
