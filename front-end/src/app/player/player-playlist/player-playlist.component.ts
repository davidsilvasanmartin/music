import { Component, input, output } from '@angular/core';

import { ApiService } from '../../api/api.service';
import type { Song } from '../../songs/song';

@Component({
  selector: 'app-player-playlist',
  template: `
    <div
      class="pointer-events-none absolute bottom-16 left-0 right-0 z-10 flex flex-row flex-nowrap justify-center"
    >
      <div
        class="pointer-events-auto relative m-2 max-h-[32rem] flex-grow-0 basis-[64rem] overflow-hidden rounded-lg bg-white shadow-lg"
      >
        <div class="flex h-full flex-col flex-nowrap">
          <div class="sticky top-0 z-10 bg-emerald-200 px-3 shadow-md">
            <div class="flex flex-row flex-nowrap items-center">
              <span class="flex-grow text-sm font-semibold">Playlist</span>
              <button
                class="rounded-full p-1 hover:bg-emerald-500"
                (click)="closePlaylist.emit()"
                aria-label="Close playlist"
              >
                <app-icon-cancel class="size-5" />
              </button>
            </div>
          </div>

          <div class="overflow-y-auto" style="max-height: calc(32rem - 3rem)">
            @if (nextSongs() && nextSongs().length > 0) {
              <div class="divide-y divide-slate-200">
                @for (song of nextSongs(); track song; let songIndex = $index) {
                  <div
                    class="flex flex-row flex-nowrap items-center gap-4 p-3 hover:bg-slate-100"
                  >
                    <div class="w-6 text-center text-slate-400">
                      {{ songIndex + 1 }}
                    </div>
                    <div class="flex-shrink-0">
                      <img
                        [src]="getSongImgUrl(song.id)"
                        alt="Album art"
                        class="size-10 rounded object-cover shadow-sm"
                        loading="lazy"
                      />
                    </div>
                    <div class="min-w-0 grow">
                      <div class="truncate font-medium">{{ song.title }}</div>
                      <div class="truncate text-sm text-slate-500">
                        {{ song.album?.album }} • {{ song.album?.albumArtist }}
                      </div>
                    </div>
                    <div class="flex flex-row gap-2">
                      <app-player-user-playlist-add-no-bg [song]="song" />
                      <app-player-playlist-remove-no-bg
                        [songIndex]="songIndex + 1"
                      />
                    </div>
                  </div>
                }
              </div>
            } @else {
              <div
                class="flex flex-col items-center justify-center p-8 text-slate-500"
              >
                <svg
                  xmlns="http://www.w3.org/2000/svg"
                  class="mb-3 size-12"
                  viewBox="0 0 24 24"
                  fill="none"
                  stroke="currentColor"
                  stroke-width="1.5"
                >
                  <path d="M9 18V5l12-2v13"></path>
                  <circle cx="6" cy="18" r="3"></circle>
                  <circle cx="18" cy="16" r="3"></circle>
                </svg>
                <p>No songs in playlist</p>
                <p class="mt-1 text-sm">Add songs to start playing</p>
              </div>
            }
          </div>
        </div>
      </div>
    </div>
  `,
  styles: [':host { display: contents; }'],
})
export class PlayerPlaylistComponent {
  nextSongs = input.required<Song[]>();
  closePlaylist = output<void>();

  constructor(private readonly apiService: ApiService) {}
  getSongImgUrl(songId: number) {
    return this.apiService.createApiUrl(`/songs/${songId}/albumArt`);
  }
}
