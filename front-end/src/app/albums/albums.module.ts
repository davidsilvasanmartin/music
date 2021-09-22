import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { AlbumsComponent } from './albums.component';
import { UiModule } from '../ui/ui.module';
import { PlaylistModule } from '../playlist';

@NgModule({
  declarations: [AlbumsComponent],
  imports: [CommonModule, UiModule, PlaylistModule],
  exports: [AlbumsComponent],
})
export class AlbumsModule {}
