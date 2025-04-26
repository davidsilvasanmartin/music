import { Component, input } from '@angular/core';

import { Store } from '@ngrx/store';

import * as queueActions from '../../queue/store/actions';
import type { QueueRootState } from '../../queue/store/state';
import type { Song } from '../../songs/song';

@Component({
  selector: 'app-queue-add',
  template: `
    <button
      class="btn rounded-full p-1 font-bold text-slate-600 hover:text-slate-400"
      aria-label="Add to play queue"
      title="Add to play queue"
      (click)="addToQueue()"
    >
      <app-icon-queue-add class="size-6" />
    </button>
  `,
})
export class QueueAddComponent {
  songs = input.required<Song[]>();

  constructor(private readonly _store: Store<QueueRootState>) {}

  addToQueue() {
    this._store.dispatch(
      queueActions.addToQueue({ songIds: this.songs().map((s) => s.id) }),
    );
  }
}
