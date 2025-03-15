import { Component, input } from '@angular/core';

import { Store } from '@ngrx/store';

import * as playlistActions from '../../player/store/actions';
import type { PlaylistRootState } from '../../player/store/state';
import type { Song } from '../../songs/song';

@Component({
  selector: 'app-player-playlist-add',
  template: `
    <button
      class="btn rounded-full border-[1px] border-slate-600 p-1 font-bold text-slate-600 hover:border-slate-400 hover:text-slate-400"
      aria-label="Add to playlist"
      title="Add to playlist"
      (click)="addToPlaylist()"
    >
      <app-icon-playlist-add class="size-5" />
    </button>
  `,
})
export class PlayerPlaylistAddComponent {
  songs = input.required<Song[]>();

  constructor(private readonly _store: Store<PlaylistRootState>) {}

  addToPlaylist() {
    this._store.dispatch(
      playlistActions.addToPlaylist({ songIds: this.songs().map((s) => s.id) }),
    );
  }
}
