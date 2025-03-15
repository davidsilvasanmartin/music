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
  styles: ':host { display: contents; }',
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
  @ViewChild('progressContainer') progressContainer!: ElementRef;
  isPlaying = signal(false);
  currentTime = signal(0);
  duration = signal(0);
  progressPercentage = computed(
    () => (this.currentTime() / this.duration()) * 100,
  );
  volume = 1;

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

  togglePlayPause() {
    if (this.audioElement?.nativeElement.paused) {
      this.audioElement.nativeElement.play();
      this.isPlaying.set(true);
    } else {
      this.audioElement?.nativeElement.pause();
      this.isPlaying.set(false);
    }
  }

  updateProgress() {
    console.log('updateProgress');
    this.currentTime.set(this.audioElement?.nativeElement.currentTime || 0);
  }

  onMetadataLoaded() {
    console.log('onMetadataLoaded');
    this.duration.set(this.audioElement?.nativeElement.duration || 0);
  }

  onPlay() {
    console.log('onPlay');
    this.isPlaying.set(true);
  }

  onPause() {
    console.log('onPause');
    this.isPlaying.set(false);
  }

  seek(event: MouseEvent) {
    const rect = this.progressContainer.nativeElement.getBoundingClientRect();
    const percent = (event.clientX - rect.left) / rect.width;
    if (this.audioElement?.nativeElement) {
      this.audioElement.nativeElement.currentTime = percent * this.duration();
    }
  }

  formatTime(seconds: number): string {
    const min = Math.floor(seconds / 60);
    const sec = Math.floor(seconds % 60);
    return `${min}:${sec < 10 ? '0' + sec : sec}`;
  }

  setVolume(event: Event) {
    const input = event.target as HTMLInputElement;
    this.volume = parseFloat(input.value);
    if (this.audioElement?.nativeElement) {
      this.audioElement.nativeElement.volume = this.volume;
    }
  }

  toggleMute() {
    if (this.audioElement?.nativeElement) {
      this.audioElement.nativeElement.muted =
        !this.audioElement?.nativeElement.muted;
    }
  }

  // You'll need to add this method if not already present
  goToPreviousSong() {
    // Implement previous song logic
  }
}
