import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';

import { IconsModule } from '../icons/icons.module';
import { PlaylistAddComponent } from './playlist-add.component';

/**
 * This module exports components to modify user playlists
 */
@NgModule({
  declarations: [PlaylistAddComponent],
  imports: [CommonModule, IconsModule],
  exports: [PlaylistAddComponent],
})
export class PlaylistComponentsModule {}
