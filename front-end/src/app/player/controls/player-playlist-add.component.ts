import { Component, input } from '@angular/core';

import { Store } from '@ngrx/store';

import { Song } from '../../songs/song';
import * as playlistActions from '../store/actions';
import { PlaylistRootState } from '../store/state';

@Component({
  selector: 'app-player-playlist-add',
  template: `
    <button
      class="btn rounded-full bg-blue-500 p-1 font-bold text-white hover:bg-blue-700"
      aria-label="Add to playlist"
      (click)="addToPlaylist()"
    >
      <app-icon-add-to-playlist />
    </button>
  `,
})
export class PlayerPlaylistAddComponent {
  songs = input.required<Song[]>();

  constructor(private readonly _store: Store<PlaylistRootState>) {}

  addToPlaylist() {
    this._store.dispatch(
      playlistActions.addToPlaylist({ songs: this.songs() }),
    );
  }
}
