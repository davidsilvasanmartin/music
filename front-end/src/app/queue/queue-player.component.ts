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

import type { Album } from '../albums/album';
import { ApiService } from '../api/api.service';
import type { Song } from '../songs/song';
import { SongsService } from '../songs/songs.service';
import * as queueActions from './store/actions';
import * as queueSelectors from './store/selectors';
import type { QueueRootState } from './store/state';

@Component({
  selector: 'app-queue-player',
  templateUrl: './queue-player.component.html',
  styles: ':host { display: contents; }',
})
export class QueuePlayerComponent implements OnDestroy, AfterViewInit {
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

  isQueueOpen = signal(false);
  @ViewChild('progressContainer') progressContainer!: ElementRef;

  isPlaying = signal(false);
  currentTime = signal(0);
  duration = signal(0);
  progressPercentage = computed(
    () => (this.currentTime() / this.duration()) * 100,
  );
  volume = signal(1);

  constructor(
    private readonly _store: Store<QueueRootState>,
    private readonly _apiService: ApiService,
    private readonly _songsService: SongsService,
  ) {
    this.currentSong = toSignal(
      this._store.pipe(select(queueSelectors.getCurrentSong)),
      { requireSync: true },
    );
    this.nextSongs = toSignal(
      this._store.pipe(select(queueSelectors.getNextSongs)),
      { requireSync: true },
    );
    this.currentSongAlbum$ = toObservable(this.currentSong).pipe(
      filter((song) => !!song),
      switchMap((song) => this._songsService.getSongAlbum(song.id)),
    );
  }

  goToNextSong() {
    this._store.dispatch(queueActions.next());
  }

  ngOnDestroy() {
    this._store.dispatch(queueActions.reset());
  }

  toggleQueue() {
    this.isQueueOpen.set(!this.isQueueOpen());
  }

  closeQueue() {
    this.isQueueOpen.set(false);
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
    if (this.audioElement?.nativeElement) {
      this.audioElement.nativeElement.volume = input.value;
    }
  }

  toggleMute() {
    if (this.audioElement?.nativeElement) {
      this.audioElement.nativeElement.muted =
        !this.audioElement?.nativeElement.muted;
    }
  }

  /**
   * This function gets called when the "volume" or "muted" properties of the
   * audio element change. Note that when the "muted" property is set to true
   * on the audio element, its "volume" property does not change
   */
  onVolumeChange() {
    if (this.audioElement?.nativeElement) {
      if (this.audioElement?.nativeElement.muted) {
        this.volume.set(0);
      } else {
        this.volume.set(this.audioElement?.nativeElement.volume);
      }
    }
  }
}
