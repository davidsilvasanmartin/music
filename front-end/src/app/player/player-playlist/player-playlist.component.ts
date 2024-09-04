import { Component, input, output } from '@angular/core';

import { Song } from '../../songs/song';

@Component({
  selector: 'app-player-playlist',
  template: `
    <div
      class="pointer-events-none absolute bottom-20 left-0 right-0 z-10 flex flex-row flex-nowrap justify-center"
    >
      <div
        class="pointer-events-auto relative m-2 flex-shrink-0 flex-grow-0 overflow-y-auto bg-emerald-50 px-2 shadow-md"
        style="flex-basis: 64rem; max-height: 32rem"
      >
        <div
          class="absolute left-0 right-0 top-0 flex flex-row justify-end"
        ></div>
        <div class="flex flex-col flex-nowrap">
          <div class="sticky top-0 bg-emerald-50 p-2">
            <div class="flex flex-row flex-nowrap">
              <span class="flex-grow">Playlist</span>
              <button (click)="closePlaylist.emit()">
                <app-icon-cancel class="size-4" />
              </button>
            </div>
          </div>
          @if (nextSongs() && nextSongs().length > 0) {
            @for (song of nextSongs(); track song) {
              {{ song.title }}
              <app-player-playlist-remove [song]="song" />
            }
          } @else {
            <div class="p-2">No songs in playlist</div>
          }
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
