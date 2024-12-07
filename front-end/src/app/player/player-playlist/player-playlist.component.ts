import { Component, input, output } from '@angular/core';

import { Song } from '../../songs/song';

@Component({
  selector: 'app-player-playlist',
  template: `
    <div
      class="pointer-events-none absolute bottom-16 left-0 right-0 z-10 flex flex-row flex-nowrap justify-center"
    >
      <div
        class="pointer-events-auto relative m-2 flex-shrink-0 flex-grow-0 overflow-y-auto bg-emerald-50 shadow-md"
        style="flex-basis: 64rem; max-height: 32rem"
      >
        <div
          class="absolute left-0 right-0 top-0 flex flex-row justify-end"
        ></div>
        <div class="flex flex-col flex-nowrap">
          <div class="sticky top-0 bg-emerald-200 p-2 shadow-md">
            <div class="flex flex-row flex-nowrap">
              <span class="flex-grow">Playlist</span>
              <button (click)="closePlaylist.emit()">
                <app-icon-cancel class="size-4" />
              </button>
            </div>
          </div>
          <div class="p-2">
            @if (nextSongs() && nextSongs().length > 0) {
              @for (song of nextSongs(); track song) {
                <div>
                  {{ song.title }} ({{ song.album?.albumArtist }} /
                  {{ song.album?.album }})
                  <app-player-playlist-remove [song]="song" />
                </div>
              }
            } @else {
              No songs in playlist
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
}
