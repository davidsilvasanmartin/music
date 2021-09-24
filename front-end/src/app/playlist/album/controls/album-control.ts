import { Directive, HostListener, Optional } from '@angular/core';
import { Subject } from 'rxjs';

import { Album } from '../../../albums/album';
import { AlbumComponent } from '../album.component';

@Directive()
export abstract class AlbumControl {
  clicked$ = new Subject<void>();

  constructor(@Optional() albumComponent: AlbumComponent) {
    if (!albumComponent) {
      throw new Error('[AlbumControl] Needs a parent AlbumComponent');
    }
    albumComponent.registerControl(this);
  }

  @HostListener('click') controlClick() {
    console.log('clicked');
    this.clicked$.next();
  }

  abstract onClickAction(song: Album): void;
}
