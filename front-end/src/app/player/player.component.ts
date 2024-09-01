import { Component, computed, OnDestroy, Signal, signal } from '@angular/core';
import { toSignal } from '@angular/core/rxjs-interop';

import { select, Store } from '@ngrx/store';

import { ApiService } from '../api/api.service';
import { Song } from '../songs/song';
import * as playlistActions from './store/actions';
import * as playlistSelectors from './store/selectors';
import { PlaylistRootState } from './store/state';

@Component({
  selector: 'app-player',
  templateUrl: './player.component.html',
})
export class PlayerComponent implements OnDestroy {
  currentSong: Signal<Song>;
  currentSongImgUrl: Signal<string> = computed(() =>
    this._apiService.createApiUrl(`/songs/${this.currentSong().id}/albumArt`),
  );
  currentSongAudioUrl: Signal<string> = computed(() =>
    this._apiService.createApiUrl(`/songs/${this.currentSong().id}/play`),
  );
  nextSongs: Signal<Song[]>;
  isPlaylistOpen = signal(false);

  constructor(
    private readonly _store: Store<PlaylistRootState>,
    private readonly _apiService: ApiService,
  ) {
    this.currentSong = toSignal(
      this._store.pipe(select(playlistSelectors.getCurrentSong)),
      { requireSync: true },
    );
    this.nextSongs = toSignal(
      this._store.pipe(select(playlistSelectors.getNextSongs)),
      { requireSync: true },
    );
  }

  goToNextSong() {
    console.log('nEXT!');
    this._store.dispatch(playlistActions.next());
  }

  ngOnDestroy() {
    this._store.dispatch(playlistActions.reset());
  }

  togglePlaylist() {
    this.isPlaylistOpen.set(!this.isPlaylistOpen());
  }

  closePlaylist() {
    this.isPlaylistOpen.set(false);
  }
}
