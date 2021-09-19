import { Component, Input, OnInit } from '@angular/core';
import { Store } from '@ngrx/store';

import { Song } from '../../songs/song';
import { PlayerRootState } from '../store/state';
import * as playerActions from '../store/actions';

@Component({
  selector: 'app-add-to-playlist-button',
  templateUrl: './add-to-playlist-button.component.html',
  styleUrls: ['./add-to-playlist-button.component.scss'],
})
export class AddToPlaylistButtonComponent implements OnInit {
  @Input() song: Song;

  constructor(private readonly _store: Store<PlayerRootState>) {}

  ngOnInit(): void {}

  addToPlaylist() {
    this._store.dispatch(playerActions.addToPlaylist({ songs: [this.song] }));
  }
}
