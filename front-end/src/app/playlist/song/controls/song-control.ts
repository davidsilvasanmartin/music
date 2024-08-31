import { Directive, HostListener, Optional } from '@angular/core';

import { Subject } from 'rxjs';

import { Song } from '../../../songs/song';
import { SongComponent } from '../song.component';

@Directive()
export abstract class SongControl {
  clicked$ = new Subject<void>();

  constructor(@Optional() songComponent: SongComponent) {
    if (!songComponent) {
      throw new Error('[SongControl] Needs a parent SongComponent');
    }
    songComponent.registerControl(this);
  }

  @HostListener('click') controlClick() {
    this.clicked$.next();
  }

  abstract onClickAction(song: Song): void;
}
