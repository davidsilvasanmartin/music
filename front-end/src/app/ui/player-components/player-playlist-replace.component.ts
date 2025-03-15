import { Component, input } from '@angular/core';

import { Store } from '@ngrx/store';

import * as playlistActions from '../../player/store/actions';
import type { PlaylistRootState } from '../../player/store/state';
import type { Song } from '../../songs/song';

@Component({
  selector: 'app-player-playlist-replace',
  template: `
    <button
      class="btn rounded-full p-1 font-bold text-slate-600 hover:text-slate-400"
      aria-label="Play"
      title="Play"
      (click)="replacePlaylist()"
    >
      <app-icon-play class="size-5" />
    </button>
  `,
})
export class PlayerPlaylistReplaceComponent {
  songs = input.required<Song[]>();

  constructor(private readonly _store: Store<PlaylistRootState>) {}

  replacePlaylist() {
    this._store.dispatch(playlistActions.reset());
    this._store.dispatch(
      playlistActions.addToPlaylist({ songIds: this.songs().map((s) => s.id) }),
    );
  }
}
