import { Component, input } from '@angular/core';

import { Store } from '@ngrx/store';

import * as playlistActions from '../../player/store/actions';
import type { PlaylistRootState } from '../../player/store/state';

@Component({
  selector: 'app-player-playlist-remove-no-bg',
  template: `
    <button
      class="btn rounded-full p-1 font-bold text-slate-500 hover:bg-slate-300"
      aria-label="Remove from playlist"
      title="Remove from playlist"
      (click)="removeSongFromPlaylist()"
    >
      <app-icon-cancel class="size-5" />
    </button>
  `,
})
export class PlayerPlaylistRemoveNoBgComponent {
  /** The index of the song in the playlist */
  songIndex = input.required<number>();

  constructor(private readonly _store: Store<PlaylistRootState>) {}

  removeSongFromPlaylist() {
    this._store.dispatch(
      playlistActions.removeFromPlaylist({ songIndex: this.songIndex() }),
    );
  }
}
