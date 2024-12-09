import { Component, input } from '@angular/core';

import { Store } from '@ngrx/store';

import * as playlistActions from '../../player/store/actions';
import type { PlaylistRootState } from '../../player/store/state';

@Component({
  selector: 'app-player-playlist-remove',
  template: `
    <button
      class="btn rounded-full bg-blue-500 p-1 font-bold text-white hover:bg-blue-700"
      aria-label="Remove from playlist"
      (click)="removeSongFromPlaylist()"
    >
      <app-icon-cancel class="size-4" />
    </button>
  `,
})
export class PlayerPlaylistRemoveComponent {
  /** The index of the song in the playlist */
  songIndex = input.required<number>();

  constructor(private readonly _store: Store<PlaylistRootState>) {}

  removeSongFromPlaylist() {
    this._store.dispatch(
      playlistActions.removeFromPlaylist({ songIndex: this.songIndex() }),
    );
  }
}
