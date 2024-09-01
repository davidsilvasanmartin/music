import { Component, Input } from '@angular/core';

import { UntilDestroy, untilDestroyed } from '@ngneat/until-destroy';

import { Song } from '../../songs/song';
import { SongControl } from './controls/song-control';

@UntilDestroy()
@Component({
  selector: 'app-song',
  templateUrl: './song.component.html',
  styleUrls: ['./song.component.scss'],
})
export class SongComponent {
  @Input() song: Song = null as any;

  registerControl(control: SongControl) {
    control.clicked$
      .pipe(untilDestroyed(this))
      .subscribe(() => control.onClickAction(this.song));
  }
}
