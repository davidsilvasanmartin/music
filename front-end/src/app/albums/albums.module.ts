import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { AlbumsComponent } from './albums.component';
import { UiModule } from '../ui/ui.module';
import { PlayerModule } from '../player/player.module';

@NgModule({
  declarations: [AlbumsComponent],
  imports: [CommonModule, UiModule, PlayerModule],
  exports: [AlbumsComponent],
})
export class AlbumsModule {}
