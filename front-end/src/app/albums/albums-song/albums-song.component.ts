import { Component, Input } from '@angular/core';

import { Song } from '../../songs/song';

@Component({
  selector: 'app-albums-song',
  templateUrl: './albums-song.component.html',
  styleUrls: ['./albums-song.component.scss'],
})
export class AlbumsSongComponent {
  @Input() song: Song = null as any;
}
