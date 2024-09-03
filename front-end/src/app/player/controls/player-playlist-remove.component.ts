import { Component, input } from '@angular/core';

import { Store } from '@ngrx/store';

import { Song } from '../../songs/song';
import * as playlistActions from '../store/actions';
import { PlaylistRootState } from '../store/state';

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
