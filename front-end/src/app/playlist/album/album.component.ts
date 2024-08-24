import { Component, Input, OnInit } from '@angular/core';
import { UntilDestroy, untilDestroyed } from '@ngneat/until-destroy';

import { Album } from '../../albums/album';
import { AlbumControl } from './controls/album-control';

@UntilDestroy()
@Component({
  selector: 'app-album',
  templateUrl: './album.component.html',
  styleUrls: ['./album.component.scss'],
})
export class AlbumComponent {
  @Input() album: Album = null as any;

  registerControl(control: AlbumControl) {
    control.clicked$
      .pipe(untilDestroyed(this))
      .subscribe(() => control.onClickAction(this.album));
  }
}
