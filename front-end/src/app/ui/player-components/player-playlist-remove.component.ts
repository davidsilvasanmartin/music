import { Component, input } from '@angular/core';

import { Store } from '@ngrx/store';

import * as playlistActions from '../../player/store/actions';
import type { PlaylistRootState } from '../../player/store/state';
import type { Song } from '../../songs/song';

@Component({
  selector: 'app-player-playlist-remove',
  template: `
    <button
      class="btn rounded-full bg-gray-300 p-1 font-bold text-gray-800 hover:bg-gray-400"
      aria-label="Remove from playlist"
      (click)="removeSongFromPlaylist()"
    >
      <app-icon-cancel class="size-4" />
    </button>
  `,
})
export class PlayerPlaylistRemoveComponent {
  song = input.required<Song>();

  constructor(private readonly _store: Store<PlaylistRootState>) {}

  removeSongFromPlaylist() {
    this._store.dispatch(
      playlistActions.removeFromPlaylist({ song: this.song() }),
    );
  }
}
