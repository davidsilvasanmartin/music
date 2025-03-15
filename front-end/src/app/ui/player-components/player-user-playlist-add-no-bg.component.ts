import { Component, input } from '@angular/core';

import { Store } from '@ngrx/store';

import type { PlaylistRootState } from '../../player/store/state';
import type { Song } from '../../songs/song';

@Component({
  selector: 'app-player-user-playlist-add-no-bg',
  template: `
    <button
      class="btn rounded-full p-1 font-bold text-slate-600 hover:text-slate-400"
      aria-label="Add to saved playlist"
      title="Add to saved playlist"
      (click)="addToUserPlaylist()"
    >
      <app-icon-user-playlist-add class="size-5" />
    </button>
  `,
})
export class PlayerUserPlaylistAddNoBgComponent {
  /** The index of the song in the playlist */
  song = input.required<Song>();

  constructor(private readonly _store: Store<PlaylistRootState>) {}

  addToUserPlaylist() {
    // TODO
  }
}
