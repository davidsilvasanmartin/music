import { Component, input } from '@angular/core';

import type { Song } from '../../songs/song';

@Component({
  selector: 'app-playlist-add',
  template: `
    <button
      class="btn rounded-full p-1 font-bold text-slate-600 hover:text-slate-400"
      aria-label="Add to playlist"
      title="Add to playlist"
      (click)="addToPlaylist()"
    >
      <app-icon-playlist-add class="size-6" />
    </button>
  `,
})
export class PlaylistAddComponent {
  song = input.required<Song>();

  addToPlaylist() {
    // TODO
  }
}
