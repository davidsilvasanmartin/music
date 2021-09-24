import { Component, Input, OnInit } from '@angular/core';
import { Store } from '@ngrx/store';

import { Song } from '../../songs/song';
import { PlaylistRootState } from '../store/state';
import * as playlistActions from '../store/actions';

@Component({
  selector: 'app-remove-from-playlist-button',
  templateUrl: './remove-from-playlist-button.component.html',
})
export class RemoveFromPlaylistButtonComponent implements OnInit {
  @Input() song: Song;

  constructor(private readonly _store: Store<PlaylistRootState>) {}

  ngOnInit(): void {}

  removeFromPlaylist() {
    this._store.dispatch(
      playlistActions.removeFromPlaylist({ song: this.song })
    );
  }
}
