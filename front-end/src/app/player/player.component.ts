import {
  AfterViewInit,
  Component,
  computed,
  ElementRef,
  OnDestroy,
  Signal,
  signal,
  ViewChild,
} from '@angular/core';
import { toObservable, toSignal } from '@angular/core/rxjs-interop';

import { select, Store } from '@ngrx/store';

import { filter, Observable } from 'rxjs';
import { switchMap } from 'rxjs/operators';

import { Album } from '../albums/album';
import { ApiService } from '../api/api.service';
import { Song } from '../songs/song';
import { SongsService } from '../songs/songs.service';
import * as playlistActions from './store/actions';
import * as playlistSelectors from './store/selectors';
import { PlaylistRootState } from './store/state';

@Component({
  selector: 'app-player',
  templateUrl: './player.component.html',
})
export class PlayerComponent implements OnDestroy, AfterViewInit {
  @ViewChild('audioElement', { static: false })
  audioElement: ElementRef | undefined;

  currentSong: Signal<Song>;
  currentSongImgUrl: Signal<string> = computed(() =>
    this._apiService.createApiUrl(`/songs/${this.currentSong().id}/albumArt`),
  );
  currentSongAudioUrl: Signal<string> = computed(() =>
    this._apiService.createApiUrl(`/songs/${this.currentSong().id}/play`),
  );
  currentSongAlbum$: Observable<Album>;
  nextSongs: Signal<Song[]>;
  isPlaylistOpen = signal(false);

  constructor(
    private readonly _store: Store<PlaylistRootState>,
    private readonly _apiService: ApiService,
    private readonly _songsService: SongsService,
  ) {
    this.currentSong = toSignal(
      this._store.pipe(select(playlistSelectors.getCurrentSong)),
      { requireSync: true },
    );
    this.nextSongs = toSignal(
      this._store.pipe(select(playlistSelectors.getNextSongs)),
      { requireSync: true },
    );
    this.currentSongAlbum$ = toObservable(this.currentSong).pipe(
      filter((song) => !!song),
      switchMap((song) => this._songsService.getSongAlbum(song.id)),
    );
  }

  goToNextSong() {
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

  ngAfterViewInit(): void {
    setInterval(() => {
      // TODO add websockets or something to send to the server chunks of listened songs. This will allow us to track which songs have been listened the most
      if (this.audioElement) {
        const htmlAudioElement: HTMLAudioElement =
          this.audioElement.nativeElement;
        console.log('------------------');
        const numChunks = htmlAudioElement.played.length;
        console.log('Chunks', numChunks);
        for (let i = 0; i < numChunks; i++) {
          console.log(
            `    ${i}     Start ->:`,
            htmlAudioElement.played.start(i),
          );
          console.log(`    ${i}     End   ->:`, htmlAudioElement.played.end(i));
        }
        console.log('------------------');
      }
    }, 3000);
  }
}
