import { Component, input, output } from '@angular/core';

import { ApiService } from '../api/api.service';
import type { Song } from '../songs/song';

@Component({
  selector: 'app-queue-list',
  template: `
    <div
      class="pointer-events-none absolute bottom-24 left-0 right-0 z-10 flex flex-row flex-nowrap justify-center"
    >
      <div
        class="pointer-events-auto relative m-2 max-h-[32rem] flex-grow-0 basis-[64rem] overflow-hidden rounded-lg border border-slate-300 bg-white shadow-xl"
      >
        <div class="flex h-full flex-col flex-nowrap">
          <div class="sticky top-0 z-10 border-b bg-slate-50 px-4 py-1">
            <div class="flex flex-row flex-nowrap items-center">
              <span class="flex-grow">Play Queue</span>
              <div class="flex gap-2">
                <button
                  *ngIf="nextSongs() && nextSongs().length > 0"
                  class="btn rounded-full p-1 text-slate-600 hover:text-slate-400"
                  (click)="clearQueue.emit()"
                  aria-label="Clear play queue"
                >
                  Clear All
                </button>
                <button
                  class="btn rounded-full text-slate-600 hover:text-slate-400"
                  (click)="closeQueue.emit()"
                  aria-label="Close play queue"
                >
                  <app-icon-chevron-down class="size-5" />
                </button>
              </div>
            </div>
          </div>

          <div class="overflow-y-auto" style="max-height: calc(32rem - 3.5rem)">
            @if (nextSongs() && nextSongs().length > 0) {
              <div class="divide-y divide-slate-200">
                @for (song of nextSongs(); track song; let songIndex = $index) {
                  <div
                    class="flex flex-row flex-nowrap items-center gap-4 p-3 transition-colors hover:bg-slate-50"
                  >
                    <div class="w-6 text-center">
                      {{ songIndex + 1 }}
                    </div>
                    <div class="flex-shrink-0">
                      <img
                        [src]="getSongImgUrl(song.id)"
                        alt="Album art"
                        class="size-12 rounded-md object-cover shadow-md"
                        loading="lazy"
                      />
                    </div>
                    <div class="min-w-0 grow">
                      <div class="truncate font-medium text-slate-800">
                        {{ song.title }}
                      </div>
                      <div
                        class="flex flex-row flex-nowrap gap-1 truncate text-sm"
                      >
                        <!-- TODO links (have a look at album.component.html !! -->
                        <a
                          [routerLink]="['.']"
                          class="text-blue-600 hover:text-blue-400 hover:underline"
                        >
                          {{ song.album?.artist?.name }}</a
                        >
                        <span>/</span>
                        <a
                          [routerLink]="['.']"
                          class="text-emerald-700 hover:text-emerald-500 hover:underline"
                          >{{ song.album?.album }}</a
                        >
                      </div>
                    </div>
                    <div class="flex flex-row gap-2">
                      <app-playlist-add [song]="song" />
                      <app-queue-remove-no-bg [songIndex]="songIndex + 1" />
                    </div>
                  </div>
                }
              </div>
            } @else {
              <div
                class="flex flex-col items-center justify-center p-10 text-slate-400"
              >
                <app-icon-eighth-note class="mb-4 size-16" />
                <p class="text-lg font-medium text-slate-600">
                  No songs in play queue
                </p>
                <p class="mt-2 text-sm">Add songs to start playing</p>
              </div>
            }
          </div>
        </div>
      </div>
    </div>
  `,
  styles: [':host { display: contents; }'],
})
export class QueueListComponent {
  nextSongs = input.required<Song[]>();
  closeQueue = output<void>();
  clearQueue = output<void>();

  constructor(private readonly apiService: ApiService) {}
  getSongImgUrl(songId: number) {
    return this.apiService.createApiUrl(`/songs/${songId}/albumArt`);
  }
}
