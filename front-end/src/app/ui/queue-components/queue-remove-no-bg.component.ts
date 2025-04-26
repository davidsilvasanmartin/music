import { Component, input } from '@angular/core';

import { Store } from '@ngrx/store';

import * as queueActions from '../../queue/store/actions';
import type { QueueRootState } from '../../queue/store/state';

@Component({
  selector: 'app-queue-remove-no-bg',
  template: `
    <button
      class="btn rounded-full p-1 font-bold text-slate-600 hover:text-slate-400"
      aria-label="Remove from play queue"
      title="Remove from play queue"
      (click)="removeSongFromQueue()"
    >
      <app-icon-cancel class="size-5" />
    </button>
  `,
})
export class QueueRemoveNoBgComponent {
  /** The index of the song in the queue */
  songIndex = input.required<number>();

  constructor(private readonly _store: Store<QueueRootState>) {}

  removeSongFromQueue() {
    this._store.dispatch(
      queueActions.removeFromQueue({ songIndex: this.songIndex() }),
    );
  }
}
