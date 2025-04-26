import { Component, input } from '@angular/core';

import { Store } from '@ngrx/store';

import * as queueActions from '../../queue/store/actions';
import type { QueueRootState } from '../../queue/store/state';
import type { Song } from '../../songs/song';

@Component({
  selector: 'app-queue-replace',
  template: `
    <button
      class="btn rounded-full p-1 font-bold text-slate-600 hover:text-slate-400"
      aria-label="Play"
      title="Play"
      (click)="replaceQueue()"
    >
      <app-icon-play class="size-5" />
    </button>
  `,
})
export class QueueReplaceComponent {
  songs = input.required<Song[]>();

  constructor(private readonly _store: Store<QueueRootState>) {}

  replaceQueue() {
    this._store.dispatch(queueActions.reset());
    this._store.dispatch(
      queueActions.addToQueue({ songIds: this.songs().map((s) => s.id) }),
    );
  }
}
